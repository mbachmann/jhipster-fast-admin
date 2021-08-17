import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankAccountFa } from '../bank-account-fa.model';
import { BankAccountFaService } from '../service/bank-account-fa.service';
import { BankAccountFaDeleteDialogComponent } from '../delete/bank-account-fa-delete-dialog.component';

@Component({
  selector: 'fa-bank-account-fa',
  templateUrl: './bank-account-fa.component.html',
})
export class BankAccountFaComponent implements OnInit {
  bankAccounts?: IBankAccountFa[];
  isLoading = false;

  constructor(protected bankAccountService: BankAccountFaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bankAccountService.query().subscribe(
      (res: HttpResponse<IBankAccountFa[]>) => {
        this.isLoading = false;
        this.bankAccounts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBankAccountFa): number {
    return item.id!;
  }

  delete(bankAccount: IBankAccountFa): void {
    const modalRef = this.modalService.open(BankAccountFaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bankAccount = bankAccount;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
