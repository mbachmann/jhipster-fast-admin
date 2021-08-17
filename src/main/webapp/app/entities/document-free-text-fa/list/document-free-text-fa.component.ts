import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentFreeTextFa } from '../document-free-text-fa.model';
import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';
import { DocumentFreeTextFaDeleteDialogComponent } from '../delete/document-free-text-fa-delete-dialog.component';

@Component({
  selector: 'fa-document-free-text-fa',
  templateUrl: './document-free-text-fa.component.html',
})
export class DocumentFreeTextFaComponent implements OnInit {
  documentFreeTexts?: IDocumentFreeTextFa[];
  isLoading = false;

  constructor(protected documentFreeTextService: DocumentFreeTextFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentFreeTextService.query().subscribe(
      (res: HttpResponse<IDocumentFreeTextFa[]>) => {
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

  trackId(index: number, item: IDocumentFreeTextFa): number {
    return item.id!;
  }

  delete(documentFreeText: IDocumentFreeTextFa): void {
    const modalRef = this.modalService.open(DocumentFreeTextFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentFreeText = documentFreeText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
