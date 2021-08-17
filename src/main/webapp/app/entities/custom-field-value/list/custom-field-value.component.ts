import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomFieldValue } from '../custom-field-value.model';
import { CustomFieldValueService } from '../service/custom-field-value.service';
import { CustomFieldValueDeleteDialogComponent } from '../delete/custom-field-value-delete-dialog.component';

@Component({
  selector: 'fa-custom-field-value',
  templateUrl: './custom-field-value.component.html',
})
export class CustomFieldValueComponent implements OnInit {
  customFieldValues?: ICustomFieldValue[];
  isLoading = false;

  constructor(protected customFieldValueService: CustomFieldValueService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.customFieldValueService.query().subscribe(
      (res: HttpResponse<ICustomFieldValue[]>) => {
        this.isLoading = false;
        this.customFieldValues = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICustomFieldValue): number {
    return item.id!;
  }

  delete(customFieldValue: ICustomFieldValue): void {
    const modalRef = this.modalService.open(CustomFieldValueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customFieldValue = customFieldValue;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
