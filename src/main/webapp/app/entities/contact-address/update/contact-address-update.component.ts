import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactAddress, ContactAddress } from '../contact-address.model';
import { ContactAddressService } from '../service/contact-address.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'fa-contact-address-update',
  templateUrl: './contact-address-update.component.html',
})
export class ContactAddressUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];

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
    inactiv: [],
    contact: [],
  });

  constructor(
    protected contactAddressService: ContactAddressService,
    protected contactService: ContactService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactAddress }) => {
      this.updateForm(contactAddress);

      this.loadRelationshipsOptions();
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

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContactAddress>>): void {
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

  protected updateForm(contactAddress: IContactAddress): void {
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
      inactiv: contactAddress.inactiv,
      contact: contactAddress.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      contactAddress.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContact[]) => this.contactService.addContactToCollectionIfMissing(contacts, this.editForm.get('contact')!.value))
      )
      .subscribe((contacts: IContact[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IContactAddress {
    return {
      ...new ContactAddress(),
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
      inactiv: this.editForm.get(['inactiv'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
