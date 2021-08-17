import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICostUnitFa, CostUnitFa } from '../cost-unit-fa.model';
import { CostUnitFaService } from '../service/cost-unit-fa.service';

@Component({
  selector: 'fa-cost-unit-fa-update',
  templateUrl: './cost-unit-fa-update.component.html',
})
export class CostUnitFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [],
    name: [],
    description: [],
    type: [],
    inactiv: [],
  });

  constructor(protected costUnitService: CostUnitFaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ costUnit }) => {
      this.updateForm(costUnit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const costUnit = this.createFromForm();
    if (costUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.costUnitService.update(costUnit));
    } else {
      this.subscribeToSaveResponse(this.costUnitService.create(costUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICostUnitFa>>): void {
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

  protected updateForm(costUnit: ICostUnitFa): void {
    this.editForm.patchValue({
      id: costUnit.id,
      remoteId: costUnit.remoteId,
      number: costUnit.number,
      name: costUnit.name,
      description: costUnit.description,
      type: costUnit.type,
      inactiv: costUnit.inactiv,
    });
  }

  protected createFromForm(): ICostUnitFa {
    return {
      ...new CostUnitFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      type: this.editForm.get(['type'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
    };
  }
}
