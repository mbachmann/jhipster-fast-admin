import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBankAccountFa, BankAccountFa } from '../bank-account-fa.model';
import { BankAccountFaService } from '../service/bank-account-fa.service';

@Component({
  selector: 'fa-bank-account-fa-update',
  templateUrl: './bank-account-fa-update.component.html',
})
export class BankAccountFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    defaultBankAccount: [],
    description: [],
    bankName: [],
    number: [],
    iban: [],
    bic: [],
    postAccount: [],
    autoSync: [],
    autoSyncDirection: [],
    currency: [],
    inactiv: [],
  });

  constructor(protected bankAccountService: BankAccountFaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      this.updateForm(bankAccount);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankAccount = this.createFromForm();
    if (bankAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.bankAccountService.update(bankAccount));
    } else {
      this.subscribeToSaveResponse(this.bankAccountService.create(bankAccount));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankAccountFa>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bankAccount: IBankAccountFa): void {
    this.editForm.patchValue({
      id: bankAccount.id,
      remoteId: bankAccount.remoteId,
      defaultBankAccount: bankAccount.defaultBankAccount,
      description: bankAccount.description,
      bankName: bankAccount.bankName,
      number: bankAccount.number,
      iban: bankAccount.iban,
      bic: bankAccount.bic,
      postAccount: bankAccount.postAccount,
      autoSync: bankAccount.autoSync,
      autoSyncDirection: bankAccount.autoSyncDirection,
      currency: bankAccount.currency,
      inactiv: bankAccount.inactiv,
    });
  }

  protected createFromForm(): IBankAccountFa {
    return {
      ...new BankAccountFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      defaultBankAccount: this.editForm.get(['defaultBankAccount'])!.value,
      description: this.editForm.get(['description'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      number: this.editForm.get(['number'])!.value,
      iban: this.editForm.get(['iban'])!.value,
      bic: this.editForm.get(['bic'])!.value,
      postAccount: this.editForm.get(['postAccount'])!.value,
      autoSync: this.editForm.get(['autoSync'])!.value,
      autoSyncDirection: this.editForm.get(['autoSyncDirection'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
    };
  }
}
