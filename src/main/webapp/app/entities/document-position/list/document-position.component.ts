import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentPosition } from '../document-position.model';
import { DocumentPositionService } from '../service/document-position.service';
import { DocumentPositionDeleteDialogComponent } from '../delete/document-position-delete-dialog.component';

@Component({
  selector: 'fa-document-position',
  templateUrl: './document-position.component.html',
})
export class DocumentPositionComponent implements OnInit {
  documentPositions?: IDocumentPosition[];
  isLoading = false;

  constructor(protected documentPositionService: DocumentPositionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentPositionService.query().subscribe(
      (res: HttpResponse<IDocumentPosition[]>) => {
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

  trackId(index: number, item: IDocumentPosition): number {
    return item.id!;
  }

  delete(documentPosition: IDocumentPosition): void {
    const modalRef = this.modalService.open(DocumentPositionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentPosition = documentPosition;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
