import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContactAddressFa, ContactAddressFa } from '../contact-address-fa.model';
import { ContactAddressFaService } from '../service/contact-address-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';

@Component({
  selector: 'fa-contact-address-fa-update',
  templateUrl: './contact-address-fa-update.component.html',
})
export class ContactAddressFaUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactFa[] = [];

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
    protected contactAddressService: ContactAddressFaService,
    protected contactService: ContactFaService,
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

  trackContactFaById(index: number, item: IContactFa): number {
    return item.id!;
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
      inactiv: contactAddress.inactiv,
      contact: contactAddress.contact,
    });

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(
      this.contactsSharedCollection,
      contactAddress.contact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContactFa[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContactFa[]) =>
          this.contactService.addContactFaToCollectionIfMissing(contacts, this.editForm.get('contact')!.value)
        )
      )
      .subscribe((contacts: IContactFa[]) => (this.contactsSharedCollection = contacts));
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
      inactiv: this.editForm.get(['inactiv'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
