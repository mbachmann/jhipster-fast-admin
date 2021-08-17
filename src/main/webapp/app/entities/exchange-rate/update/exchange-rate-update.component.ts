import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExchangeRate, ExchangeRate } from '../exchange-rate.model';
import { ExchangeRateService } from '../service/exchange-rate.service';

@Component({
  selector: 'fa-exchange-rate-update',
  templateUrl: './exchange-rate-update.component.html',
})
export class ExchangeRateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    currencyFrom: [],
    currencyTo: [],
    rate: [],
    created: [],
    inactiv: [],
  });

  constructor(protected exchangeRateService: ExchangeRateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exchangeRate }) => {
      if (exchangeRate.id === undefined) {
        const today = dayjs().startOf('day');
        exchangeRate.created = today;
      }

      this.updateForm(exchangeRate);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exchangeRate = this.createFromForm();
    if (exchangeRate.id !== undefined) {
      this.subscribeToSaveResponse(this.exchangeRateService.update(exchangeRate));
    } else {
      this.subscribeToSaveResponse(this.exchangeRateService.create(exchangeRate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExchangeRate>>): void {
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

  protected updateForm(exchangeRate: IExchangeRate): void {
    this.editForm.patchValue({
      id: exchangeRate.id,
      remoteId: exchangeRate.remoteId,
      currencyFrom: exchangeRate.currencyFrom,
      currencyTo: exchangeRate.currencyTo,
      rate: exchangeRate.rate,
      created: exchangeRate.created ? exchangeRate.created.format(DATE_TIME_FORMAT) : null,
      inactiv: exchangeRate.inactiv,
    });
  }

  protected createFromForm(): IExchangeRate {
    return {
      ...new ExchangeRate(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      currencyFrom: this.editForm.get(['currencyFrom'])!.value,
      currencyTo: this.editForm.get(['currencyTo'])!.value,
      rate: this.editForm.get(['rate'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      inactiv: this.editForm.get(['inactiv'])!.value,
    };
  }
}
