import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentPositionFa } from '../document-position-fa.model';
import { DocumentPositionFaService } from '../service/document-position-fa.service';
import { DocumentPositionFaDeleteDialogComponent } from '../delete/document-position-fa-delete-dialog.component';

@Component({
  selector: 'fa-document-position-fa',
  templateUrl: './document-position-fa.component.html',
})
export class DocumentPositionFaComponent implements OnInit {
  documentPositions?: IDocumentPositionFa[];
  isLoading = false;

  constructor(protected documentPositionService: DocumentPositionFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentPositionService.query().subscribe(
      (res: HttpResponse<IDocumentPositionFa[]>) => {
        this.isLoading = false;
        this.documentPositions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDocumentPositionFa): number {
    return item.id!;
  }

  delete(documentPosition: IDocumentPositionFa): void {
    const modalRef = this.modalService.open(DocumentPositionFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentPosition = documentPosition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
