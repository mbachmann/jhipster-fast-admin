import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IIsr, Isr } from '../isr.model';
import { IsrService } from '../service/isr.service';

@Component({
  selector: 'fa-isr-update',
  templateUrl: './isr-update.component.html',
})
export class IsrUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    defaultIsr: [],
    type: [],
    position: [],
    name: [],
    bankName: [],
    bankAddress: [],
    recipientName: [],
    recipientAddition: [],
    recipientStreet: [],
    recipientCity: [],
    deliveryNumber: [],
    iban: [],
    subscriberNumber: [],
    leftPrintAdjust: [],
    topPrintAdjust: [],
    created: [],
    inactiv: [],
  });

  constructor(protected isrService: IsrService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isr }) => {
      if (isr.id === undefined) {
        const today = dayjs().startOf('day');
        isr.created = today;
      }

      this.updateForm(isr);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const isr = this.createFromForm();
    if (isr.id !== undefined) {
      this.subscribeToSaveResponse(this.isrService.update(isr));
    } else {
      this.subscribeToSaveResponse(this.isrService.create(isr));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIsr>>): void {
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

  protected updateForm(isr: IIsr): void {
    this.editForm.patchValue({
      id: isr.id,
      defaultIsr: isr.defaultIsr,
      type: isr.type,
      position: isr.position,
      name: isr.name,
      bankName: isr.bankName,
      bankAddress: isr.bankAddress,
      recipientName: isr.recipientName,
      recipientAddition: isr.recipientAddition,
      recipientStreet: isr.recipientStreet,
      recipientCity: isr.recipientCity,
      deliveryNumber: isr.deliveryNumber,
      iban: isr.iban,
      subscriberNumber: isr.subscriberNumber,
      leftPrintAdjust: isr.leftPrintAdjust,
      topPrintAdjust: isr.topPrintAdjust,
      created: isr.created ? isr.created.format(DATE_TIME_FORMAT) : null,
      inactiv: isr.inactiv,
    });
  }

  protected createFromForm(): IIsr {
    return {
      ...new Isr(),
      id: this.editForm.get(['id'])!.value,
      defaultIsr: this.editForm.get(['defaultIsr'])!.value,
      type: this.editForm.get(['type'])!.value,
      position: this.editForm.get(['position'])!.value,
      name: this.editForm.get(['name'])!.value,
      bankName: this.editForm.get(['bankName'])!.value,
      bankAddress: this.editForm.get(['bankAddress'])!.value,
      recipientName: this.editForm.get(['recipientName'])!.value,
      recipientAddition: this.editForm.get(['recipientAddition'])!.value,
      recipientStreet: this.editForm.get(['recipientStreet'])!.value,
      recipientCity: this.editForm.get(['recipientCity'])!.value,
      deliveryNumber: this.editForm.get(['deliveryNumber'])!.value,
      iban: this.editForm.get(['iban'])!.value,
      subscriberNumber: this.editForm.get(['subscriberNumber'])!.value,
      leftPrintAdjust: this.editForm.get(['leftPrintAdjust'])!.value,
      topPrintAdjust: this.editForm.get(['topPrintAdjust'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      inactiv: this.editForm.get(['inactiv'])!.value,
    };
  }
}
