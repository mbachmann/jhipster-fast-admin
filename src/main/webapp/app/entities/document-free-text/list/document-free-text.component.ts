import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentFreeText } from '../document-free-text.model';
import { DocumentFreeTextService } from '../service/document-free-text.service';
import { DocumentFreeTextDeleteDialogComponent } from '../delete/document-free-text-delete-dialog.component';

@Component({
  selector: 'fa-document-free-text',
  templateUrl: './document-free-text.component.html',
})
export class DocumentFreeTextComponent implements OnInit {
  documentFreeTexts?: IDocumentFreeText[];
  isLoading = false;

  constructor(protected documentFreeTextService: DocumentFreeTextService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentFreeTextService.query().subscribe(
      (res: HttpResponse<IDocumentFreeText[]>) => {
        this.isLoading = false;
        this.documentFreeTexts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDocumentFreeText): number {
    return item.id!;
  }

  delete(documentFreeText: IDocumentFreeText): void {
    const modalRef = this.modalService.open(DocumentFreeTextDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentFreeText = documentFreeText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
