import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICatalogUnit, CatalogUnit } from '../catalog-unit.model';
import { CatalogUnitService } from '../service/catalog-unit.service';

@Component({
  selector: 'fa-catalog-unit-update',
  templateUrl: './catalog-unit-update.component.html',
})
export class CatalogUnitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    name: [null, [Validators.required]],
    nameDe: [],
    nameEn: [],
    nameFr: [],
    nameIt: [],
    scope: [],
    custom: [],
    inactiv: [],
  });

  constructor(protected catalogUnitService: CatalogUnitService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogUnit }) => {
      this.updateForm(catalogUnit);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogUnit = this.createFromForm();
    if (catalogUnit.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogUnitService.update(catalogUnit));
    } else {
      this.subscribeToSaveResponse(this.catalogUnitService.create(catalogUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogUnit>>): void {
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

  protected updateForm(catalogUnit: ICatalogUnit): void {
    this.editForm.patchValue({
      id: catalogUnit.id,
      remoteId: catalogUnit.remoteId,
      name: catalogUnit.name,
      nameDe: catalogUnit.nameDe,
      nameEn: catalogUnit.nameEn,
      nameFr: catalogUnit.nameFr,
      nameIt: catalogUnit.nameIt,
      scope: catalogUnit.scope,
      custom: catalogUnit.custom,
      inactiv: catalogUnit.inactiv,
    });
  }

  protected createFromForm(): ICatalogUnit {
    return {
      ...new CatalogUnit(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      name: this.editForm.get(['name'])!.value,
      nameDe: this.editForm.get(['nameDe'])!.value,
      nameEn: this.editForm.get(['nameEn'])!.value,
      nameFr: this.editForm.get(['nameFr'])!.value,
      nameIt: this.editForm.get(['nameIt'])!.value,
      scope: this.editForm.get(['scope'])!.value,
      custom: this.editForm.get(['custom'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
    };
  }
}
