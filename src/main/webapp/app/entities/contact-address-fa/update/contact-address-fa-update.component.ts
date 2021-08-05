import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IContactAddressFa, ContactAddressFa } from '../contact-address-fa.model';
import { ContactAddressFaService } from '../service/contact-address-fa.service';

@Component({
  selector: 'fa-contact-address-fa-update',
  templateUrl: './contact-address-fa-update.component.html',
})
export class ContactAddressFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    defaultAddress: [null, [Validators.required]],
    country: [null, [Validators.required]],
    street: [],
    streetNo: [],
    street2: [],
    postcode: [],
    city: [],
    hideOnDocuments: [null, [Validators.required]],
    defaultPrepage: [null, [Validators.required]],
  });

  constructor(
    protected contactAddressService: ContactAddressFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactAddress }) => {
      this.updateForm(contactAddress);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contactAddress = this.createFromForm();
    if (contactAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.contactAddressService.update(contactAddress));
    } else {
      this.subscribeToSaveResponse(this.contactAddressService.create(contactAddress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactAddressFa>>): void {
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

  protected updateForm(contactAddress: IContactAddressFa): void {
    this.editForm.patchValue({
      id: contactAddress.id,
      remoteId: contactAddress.remoteId,
      defaultAddress: contactAddress.defaultAddress,
      country: contactAddress.country,
      street: contactAddress.street,
      streetNo: contactAddress.streetNo,
      street2: contactAddress.street2,
      postcode: contactAddress.postcode,
      city: contactAddress.city,
      hideOnDocuments: contactAddress.hideOnDocuments,
      defaultPrepage: contactAddress.defaultPrepage,
    });
  }

  protected createFromForm(): IContactAddressFa {
    return {
      ...new ContactAddressFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      defaultAddress: this.editForm.get(['defaultAddress'])!.value,
      country: this.editForm.get(['country'])!.value,
      street: this.editForm.get(['street'])!.value,
      streetNo: this.editForm.get(['streetNo'])!.value,
      street2: this.editForm.get(['street2'])!.value,
      postcode: this.editForm.get(['postcode'])!.value,
      city: this.editForm.get(['city'])!.value,
      hideOnDocuments: this.editForm.get(['hideOnDocuments'])!.value,
      defaultPrepage: this.editForm.get(['defaultPrepage'])!.value,
    };
  }
}
