import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactAccountFa } from '../contact-account-fa.model';

@Component({
  selector: 'fa-contact-account-fa-detail',
  templateUrl: './contact-account-fa-detail.component.html',
})
export class ContactAccountFaDetailComponent implements OnInit {
  contactAccount: IContactAccountFa | null = null;

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
