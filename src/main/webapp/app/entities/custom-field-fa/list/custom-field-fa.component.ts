import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomFieldFa } from '../custom-field-fa.model';
import { CustomFieldFaService } from '../service/custom-field-fa.service';
import { CustomFieldFaDeleteDialogComponent } from '../delete/custom-field-fa-delete-dialog.component';

@Component({
  selector: 'fa-custom-field-fa',
  templateUrl: './custom-field-fa.component.html',
})
export class CustomFieldFaComponent implements OnInit {
  customFields?: ICustomFieldFa[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected customFieldService: CustomFieldFaService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.customFieldService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<ICustomFieldFa[]>) => {
            this.isLoading = false;
            this.customFields = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.customFieldService.query().subscribe(
      (res: HttpResponse<ICustomFieldFa[]>) => {
        this.isLoading = false;
        this.customFields = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICustomFieldFa): number {
    return item.id!;
  }

  delete(customField: ICustomFieldFa): void {
    const modalRef = this.modalService.open(CustomFieldFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customField = customField;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
