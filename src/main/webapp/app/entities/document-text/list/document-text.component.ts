import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentText } from '../document-text.model';
import { DocumentTextService } from '../service/document-text.service';
import { DocumentTextDeleteDialogComponent } from '../delete/document-text-delete-dialog.component';

@Component({
  selector: 'fa-document-text',
  templateUrl: './document-text.component.html',
})
export class DocumentTextComponent implements OnInit {
  documentTexts?: IDocumentText[];
  isLoading = false;

  constructor(protected documentTextService: DocumentTextService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentTextService.query().subscribe(
      (res: HttpResponse<IDocumentText[]>) => {
        this.isLoading = false;
        this.documentTexts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDocumentText): number {
    return item.id!;
  }

  delete(documentText: IDocumentText): void {
    const modalRef = this.modalService.open(DocumentTextDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentText = documentText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
