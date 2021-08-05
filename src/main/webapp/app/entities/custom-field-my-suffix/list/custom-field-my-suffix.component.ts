import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICustomFieldMySuffix } from '../custom-field-my-suffix.model';
import { CustomFieldMySuffixService } from '../service/custom-field-my-suffix.service';
import { CustomFieldMySuffixDeleteDialogComponent } from '../delete/custom-field-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-custom-field-my-suffix',
  templateUrl: './custom-field-my-suffix.component.html',
})
export class CustomFieldMySuffixComponent implements OnInit {
  customFields?: ICustomFieldMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected customFieldService: CustomFieldMySuffixService,
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
          (res: HttpResponse<ICustomFieldMySuffix[]>) => {
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
      (res: HttpResponse<ICustomFieldMySuffix[]>) => {
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

  trackId(index: number, item: ICustomFieldMySuffix): number {
    return item.id!;
  }

  delete(customField: ICustomFieldMySuffix): void {
    const modalRef = this.modalService.open(CustomFieldMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.customField = customField;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
