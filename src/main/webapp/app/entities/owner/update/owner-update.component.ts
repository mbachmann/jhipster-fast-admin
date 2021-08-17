import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOwner, Owner } from '../owner.model';
import { OwnerService } from '../service/owner.service';

@Component({
  selector: 'fa-owner-update',
  templateUrl: './owner-update.component.html',
})
export class OwnerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    name: [],
    surname: [],
    email: [],
    language: [],
    companyName: [],
    companyAddition: [],
    companyCountry: [],
    companyStreet: [],
    companyStreetNo: [],
    companyStreet2: [],
    companyPostcode: [],
    companyCity: [],
    companyPhone: [],
    companyFax: [],
    companyEmail: [],
    companyWebsite: [],
    companyVatId: [],
    companyCurrency: [],
  });

  constructor(protected ownerService: OwnerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ owner }) => {
      this.updateForm(owner);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const owner = this.createFromForm();
    if (owner.id !== undefined) {
      this.subscribeToSaveResponse(this.ownerService.update(owner));
    } else {
      this.subscribeToSaveResponse(this.ownerService.create(owner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOwner>>): void {
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

  protected updateForm(owner: IOwner): void {
    this.editForm.patchValue({
      id: owner.id,
      remoteId: owner.remoteId,
      name: owner.name,
      surname: owner.surname,
      email: owner.email,
      language: owner.language,
      companyName: owner.companyName,
      companyAddition: owner.companyAddition,
      companyCountry: owner.companyCountry,
      companyStreet: owner.companyStreet,
      companyStreetNo: owner.companyStreetNo,
      companyStreet2: owner.companyStreet2,
      companyPostcode: owner.companyPostcode,
      companyCity: owner.companyCity,
      companyPhone: owner.companyPhone,
      companyFax: owner.companyFax,
      companyEmail: owner.companyEmail,
      companyWebsite: owner.companyWebsite,
      companyVatId: owner.companyVatId,
      companyCurrency: owner.companyCurrency,
    });
  }

  protected createFromForm(): IOwner {
    return {
      ...new Owner(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      email: this.editForm.get(['email'])!.value,
      language: this.editForm.get(['language'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      companyAddition: this.editForm.get(['companyAddition'])!.value,
      companyCountry: this.editForm.get(['companyCountry'])!.value,
      companyStreet: this.editForm.get(['companyStreet'])!.value,
      companyStreetNo: this.editForm.get(['companyStreetNo'])!.value,
      companyStreet2: this.editForm.get(['companyStreet2'])!.value,
      companyPostcode: this.editForm.get(['companyPostcode'])!.value,
      companyCity: this.editForm.get(['companyCity'])!.value,
      companyPhone: this.editForm.get(['companyPhone'])!.value,
      companyFax: this.editForm.get(['companyFax'])!.value,
      companyEmail: this.editForm.get(['companyEmail'])!.value,
      companyWebsite: this.editForm.get(['companyWebsite'])!.value,
      companyVatId: this.editForm.get(['companyVatId'])!.value,
      companyCurrency: this.editForm.get(['companyCurrency'])!.value,
    };
  }
}
