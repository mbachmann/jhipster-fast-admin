import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentTextFa } from '../document-text-fa.model';
import { DocumentTextFaService } from '../service/document-text-fa.service';
import { DocumentTextFaDeleteDialogComponent } from '../delete/document-text-fa-delete-dialog.component';

@Component({
  selector: 'fa-document-text-fa',
  templateUrl: './document-text-fa.component.html',
})
export class DocumentTextFaComponent implements OnInit {
  documentTexts?: IDocumentTextFa[];
  isLoading = false;

  constructor(protected documentTextService: DocumentTextFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.documentTextService.query().subscribe(
      (res: HttpResponse<IDocumentTextFa[]>) => {
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

  trackId(index: number, item: IDocumentTextFa): number {
    return item.id!;
  }

  delete(documentText: IDocumentTextFa): void {
    const modalRef = this.modalService.open(DocumentTextFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentText = documentText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
