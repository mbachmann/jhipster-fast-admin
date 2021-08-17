import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactAccount } from '../contact-account.model';

@Component({
  selector: 'fa-contact-account-detail',
  templateUrl: './contact-account-detail.component.html',
})
export class ContactAccountDetailComponent implements OnInit {
  contactAccount: IContactAccount | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactAccount }) => {
      this.contactAccount = contactAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
