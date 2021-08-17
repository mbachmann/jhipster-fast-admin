import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';
import { DescriptiveDocumentTextFaService } from '../service/descriptive-document-text-fa.service';
import { DescriptiveDocumentTextFaDeleteDialogComponent } from '../delete/descriptive-document-text-fa-delete-dialog.component';

@Component({
  selector: 'fa-descriptive-document-text-fa',
  templateUrl: './descriptive-document-text-fa.component.html',
})
export class DescriptiveDocumentTextFaComponent implements OnInit {
  descriptiveDocumentTexts?: IDescriptiveDocumentTextFa[];
  isLoading = false;

  constructor(protected descriptiveDocumentTextService: DescriptiveDocumentTextFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.descriptiveDocumentTextService.query().subscribe(
      (res: HttpResponse<IDescriptiveDocumentTextFa[]>) => {
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

  trackId(index: number, item: IDescriptiveDocumentTextFa): number {
    return item.id!;
  }

  delete(descriptiveDocumentText: IDescriptiveDocumentTextFa): void {
    const modalRef = this.modalService.open(DescriptiveDocumentTextFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.descriptiveDocumentText = descriptiveDocumentText;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
