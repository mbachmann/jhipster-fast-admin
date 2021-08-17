import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBankAccountFa } from '../bank-account-fa.model';

@Component({
  selector: 'fa-bank-account-fa-detail',
  templateUrl: './bank-account-fa-detail.component.html',
})
export class BankAccountFaDetailComponent implements OnInit {
  bankAccount: IBankAccountFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      this.bankAccount = bankAccount;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
