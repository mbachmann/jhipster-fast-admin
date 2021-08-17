import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactReminderFa } from '../contact-reminder-fa.model';

@Component({
  selector: 'fa-contact-reminder-fa-detail',
  templateUrl: './contact-reminder-fa-detail.component.html',
})
export class ContactReminderFaDetailComponent implements OnInit {
  contactReminder: IContactReminderFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactReminder }) => {
      this.contactReminder = contactReminder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
