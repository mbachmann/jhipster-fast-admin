import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactReminder } from '../contact-reminder.model';

@Component({
  selector: 'fa-contact-reminder-detail',
  templateUrl: './contact-reminder-detail.component.html',
})
export class ContactReminderDetailComponent implements OnInit {
  contactReminder: IContactReminder | null = null;

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
