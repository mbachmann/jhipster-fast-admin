import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomField } from '../custom-field.model';
import { CustomFieldService } from '../service/custom-field.service';
import { CustomFieldDeleteDialogComponent } from '../delete/custom-field-delete-dialog.component';

@Component({
  selector: 'fa-custom-field',
  templateUrl: './custom-field.component.html',
})
export class CustomFieldComponent implements OnInit {
  customFields?: ICustomField[];
  isLoading = false;

  constructor(protected customFieldService: CustomFieldService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.customFieldService.query().subscribe(
      (res: HttpResponse<ICustomField[]>) => {
        this.isLoading = false;
        this.customFields = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICustomField): number {
    return item.id!;
  }

  delete(customField: ICustomField): void {
    const modalRef = this.modalService.open(CustomFieldDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customField = customField;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
