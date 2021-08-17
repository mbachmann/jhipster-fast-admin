import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDescriptiveDocumentText } from '../descriptive-document-text.model';
import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';
import { DescriptiveDocumentTextDeleteDialogComponent } from '../delete/descriptive-document-text-delete-dialog.component';

@Component({
  selector: 'fa-descriptive-document-text',
  templateUrl: './descriptive-document-text.component.html',
})
export class DescriptiveDocumentTextComponent implements OnInit {
  descriptiveDocumentTexts?: IDescriptiveDocumentText[];
  isLoading = false;

  constructor(protected descriptiveDocumentTextService: DescriptiveDocumentTextService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.descriptiveDocumentTextService.query().subscribe(
      (res: HttpResponse<IDescriptiveDocumentText[]>) => {
        this.isLoading = false;
        this.descriptiveDocumentTexts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDescriptiveDocumentText): number {
    return item.id!;
  }

  delete(descriptiveDocumentText: IDescriptiveDocumentText): void {
    const modalRef = this.modalService.open(DescriptiveDocumentTextDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.descriptiveDocumentText = descriptiveDocumentText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
