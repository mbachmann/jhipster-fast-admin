import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaskMySuffix } from '../task-my-suffix.model';
import { TaskMySuffixService } from '../service/task-my-suffix.service';
import { TaskMySuffixDeleteDialogComponent } from '../delete/task-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-task-my-suffix',
  templateUrl: './task-my-suffix.component.html',
})
export class TaskMySuffixComponent implements OnInit {
  tasks?: ITaskMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(protected taskService: TaskMySuffixService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.taskService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<ITaskMySuffix[]>) => {
            this.isLoading = false;
            this.tasks = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.taskService.query().subscribe(
      (res: HttpResponse<ITaskMySuffix[]>) => {
        this.isLoading = false;
        this.tasks = res.body ?? [];
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

  trackId(index: number, item: ITaskMySuffix): number {
    return item.id!;
  }

  delete(task: ITaskMySuffix): void {
    const modalRef = this.modalService.open(TaskMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.task = task;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
