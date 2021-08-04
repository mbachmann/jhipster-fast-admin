import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepartmentMySuffix } from '../department-my-suffix.model';
import { DepartmentMySuffixService } from '../service/department-my-suffix.service';
import { DepartmentMySuffixDeleteDialogComponent } from '../delete/department-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-department-my-suffix',
  templateUrl: './department-my-suffix.component.html',
})
export class DepartmentMySuffixComponent implements OnInit {
  departments?: IDepartmentMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected departmentService: DepartmentMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.departmentService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IDepartmentMySuffix[]>) => {
            this.isLoading = false;
            this.departments = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.departmentService.query().subscribe(
      (res: HttpResponse<IDepartmentMySuffix[]>) => {
        this.isLoading = false;
        this.departments = res.body ?? [];
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

  trackId(index: number, item: IDepartmentMySuffix): number {
    return item.id!;
  }

  delete(department: IDepartmentMySuffix): void {
    const modalRef = this.modalService.open(DepartmentMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.department = department;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
