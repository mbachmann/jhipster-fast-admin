import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IValueAddedTax, ValueAddedTax } from '../value-added-tax.model';
import { ValueAddedTaxService } from '../service/value-added-tax.service';

@Component({
  selector: 'fa-value-added-tax-update',
  templateUrl: './value-added-tax-update.component.html',
})
export class ValueAddedTaxUpdateComponent implements OnInit {
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

  constructor(protected valueAddedTaxService: ValueAddedTaxService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ valueAddedTax }) => {
      this.updateForm(valueAddedTax);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const valueAddedTax = this.createFromForm();
    if (valueAddedTax.id !== undefined) {
      this.subscribeToSaveResponse(this.valueAddedTaxService.update(valueAddedTax));
    } else {
      this.subscribeToSaveResponse(this.valueAddedTaxService.create(valueAddedTax));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IValueAddedTax>>): void {
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

  protected updateForm(valueAddedTax: IValueAddedTax): void {
    this.editForm.patchValue({
      id: valueAddedTax.id,
      name: valueAddedTax.name,
      vatType: valueAddedTax.vatType,
      validFrom: valueAddedTax.validFrom,
      validUntil: valueAddedTax.validUntil,
      vatPercent: valueAddedTax.vatPercent,
      inactiv: valueAddedTax.inactiv,
      newVatId: valueAddedTax.newVatId,
    });
  }

  protected createFromForm(): IValueAddedTax {
    return {
      ...new ValueAddedTax(),
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
