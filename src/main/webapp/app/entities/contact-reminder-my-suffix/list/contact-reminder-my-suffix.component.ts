import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactReminderMySuffix } from '../contact-reminder-my-suffix.model';
import { ContactReminderMySuffixService } from '../service/contact-reminder-my-suffix.service';
import { ContactReminderMySuffixDeleteDialogComponent } from '../delete/contact-reminder-my-suffix-delete-dialog.component';

@Component({
  selector: 'jhl-contact-reminder-my-suffix',
  templateUrl: './contact-reminder-my-suffix.component.html',
})
export class ContactReminderMySuffixComponent implements OnInit {
  contactReminders?: IContactReminderMySuffix[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactReminderService: ContactReminderMySuffixService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.contactReminderService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IContactReminderMySuffix[]>) => {
            this.isLoading = false;
            this.contactReminders = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.contactReminderService.query().subscribe(
      (res: HttpResponse<IContactReminderMySuffix[]>) => {
        this.isLoading = false;
        this.contactReminders = res.body ?? [];
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

  trackId(index: number, item: IContactReminderMySuffix): number {
    return item.id!;
  }

  delete(contactReminder: IContactReminderMySuffix): void {
    const modalRef = this.modalService.open(ContactReminderMySuffixDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactReminder = contactReminder;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
