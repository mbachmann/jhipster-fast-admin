import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IContactReminderFa } from '../contact-reminder-fa.model';
import { ContactReminderFaService } from '../service/contact-reminder-fa.service';
import { ContactReminderFaDeleteDialogComponent } from '../delete/contact-reminder-fa-delete-dialog.component';

@Component({
  selector: 'fa-contact-reminder-fa',
  templateUrl: './contact-reminder-fa.component.html',
})
export class ContactReminderFaComponent implements OnInit {
  contactReminders?: IContactReminderFa[];
  isLoading = false;
  currentSearch: string;

  constructor(
    protected contactReminderService: ContactReminderFaService,
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
          (res: HttpResponse<IContactReminderFa[]>) => {
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
      (res: HttpResponse<IContactReminderFa[]>) => {
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

  trackId(index: number, item: IContactReminderFa): number {
    return item.id!;
  }

  delete(contactReminder: IContactReminderFa): void {
    const modalRef = this.modalService.open(ContactReminderFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.contactReminder = contactReminder;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
