import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVatFa, VatFa } from '../vat-fa.model';
import { VatFaService } from '../service/vat-fa.service';

@Component({
  selector: 'fa-vat-fa-update',
  templateUrl: './vat-fa-update.component.html',
})
export class VatFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    vatType: [],
    validFrom: [],
    validUntil: [],
    vatPercent: [],
    inactiv: [],
    newVatId: [],
  });

  constructor(protected vatService: VatFaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vat }) => {
      this.updateForm(vat);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vat = this.createFromForm();
    if (vat.id !== undefined) {
      this.subscribeToSaveResponse(this.vatService.update(vat));
    } else {
      this.subscribeToSaveResponse(this.vatService.create(vat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVatFa>>): void {
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

  protected updateForm(vat: IVatFa): void {
    this.editForm.patchValue({
      id: vat.id,
      name: vat.name,
      vatType: vat.vatType,
      validFrom: vat.validFrom,
      validUntil: vat.validUntil,
      vatPercent: vat.vatPercent,
      inactiv: vat.inactiv,
      newVatId: vat.newVatId,
    });
  }

  protected createFromForm(): IVatFa {
    return {
      ...new VatFa(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      vatType: this.editForm.get(['vatType'])!.value,
      validFrom: this.editForm.get(['validFrom'])!.value,
      validUntil: this.editForm.get(['validUntil'])!.value,
      vatPercent: this.editForm.get(['vatPercent'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
      newVatId: this.editForm.get(['newVatId'])!.value,
    };
  }
}
