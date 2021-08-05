import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactReminderMySuffix } from '../contact-reminder-my-suffix.model';

@Component({
  selector: 'jhl-contact-reminder-my-suffix-detail',
  templateUrl: './contact-reminder-my-suffix-detail.component.html',
})
export class ContactReminderMySuffixDetailComponent implements OnInit {
  contactReminder: IContactReminderMySuffix | null = null;

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
