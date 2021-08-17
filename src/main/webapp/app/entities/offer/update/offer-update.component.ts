import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOffer, Offer } from '../offer.model';
import { OfferService } from '../service/offer.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';
import { IContactAddress } from 'app/entities/contact-address/contact-address.model';
import { ContactAddressService } from 'app/entities/contact-address/service/contact-address.service';
import { IContactPerson } from 'app/entities/contact-person/contact-person.model';
import { ContactPersonService } from 'app/entities/contact-person/service/contact-person.service';
import { ILayout } from 'app/entities/layout/layout.model';
import { LayoutService } from 'app/entities/layout/service/layout.service';
import { ISignature } from 'app/entities/signature/signature.model';
import { SignatureService } from 'app/entities/signature/service/signature.service';

@Component({
  selector: 'fa-offer-update',
  templateUrl: './offer-update.component.html',
})
export class OfferUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];
  contactAddressesSharedCollection: IContactAddress[] = [];
  contactPeopleSharedCollection: IContactPerson[] = [];
  layoutsSharedCollection: ILayout[] = [];
  signaturesSharedCollection: ISignature[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [null, []],
    contactName: [],
    date: [],
    validUntil: [],
    periodText: [],
    currency: [],
    total: [],
    vatIncluded: [],
    discountRate: [],
    discountType: [],
    acceptOnline: [],
    acceptOnlineUrl: [],
    acceptOnlineStatus: [],
    language: [],
    pageAmount: [],
    notes: [],
    status: [],
    created: [],
    contact: [],
    contactAddress: [],
    contactPerson: [],
    contactPrePageAddress: [],
    layout: [],
    layout: [],
  });

  constructor(
    protected offerService: OfferService,
    protected contactService: ContactService,
    protected contactAddressService: ContactAddressService,
    protected contactPersonService: ContactPersonService,
    protected layoutService: LayoutService,
    protected signatureService: SignatureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ offer }) => {
      if (offer.id === undefined) {
        const today = dayjs().startOf('day');
        offer.created = today;
      }

      this.updateForm(offer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const offer = this.createFromForm();
    if (offer.id !== undefined) {
      this.subscribeToSaveResponse(this.offerService.update(offer));
    } else {
      this.subscribeToSaveResponse(this.offerService.create(offer));
    }
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  trackContactAddressById(index: number, item: IContactAddress): number {
    return item.id!;
  }

  trackContactPersonById(index: number, item: IContactPerson): number {
    return item.id!;
  }

  trackLayoutById(index: number, item: ILayout): number {
    return item.id!;
  }

  trackSignatureById(index: number, item: ISignature): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOffer>>): void {
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

  protected updateForm(offer: IOffer): void {
    this.editForm.patchValue({
      id: offer.id,
      remoteId: offer.remoteId,
      number: offer.number,
      contactName: offer.contactName,
      date: offer.date,
      validUntil: offer.validUntil,
      periodText: offer.periodText,
      currency: offer.currency,
      total: offer.total,
      vatIncluded: offer.vatIncluded,
      discountRate: offer.discountRate,
      discountType: offer.discountType,
      acceptOnline: offer.acceptOnline,
      acceptOnlineUrl: offer.acceptOnlineUrl,
      acceptOnlineStatus: offer.acceptOnlineStatus,
      language: offer.language,
      pageAmount: offer.pageAmount,
      notes: offer.notes,
      status: offer.status,
      created: offer.created ? offer.created.format(DATE_TIME_FORMAT) : null,
      contact: offer.contact,
      contactAddress: offer.contactAddress,
      contactPerson: offer.contactPerson,
      contactPrePageAddress: offer.contactPrePageAddress,
      layout: offer.layout,
      layout: offer.layout,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(this.contactsSharedCollection, offer.contact);
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      offer.contactAddress,
      offer.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      offer.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutToCollectionIfMissing(this.layoutsSharedCollection, offer.layout);
    this.signaturesSharedCollection = this.signatureService.addSignatureToCollectionIfMissing(
      this.signaturesSharedCollection,
      offer.layout
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

    this.contactAddressService
      .query()
      .pipe(map((res: HttpResponse<IContactAddress[]>) => res.body ?? []))
      .pipe(
        map((contactAddresses: IContactAddress[]) =>
          this.contactAddressService.addContactAddressToCollectionIfMissing(
            contactAddresses,
            this.editForm.get('contactAddress')!.value,
            this.editForm.get('contactPrePageAddress')!.value
          )
        )
      )
      .subscribe((contactAddresses: IContactAddress[]) => (this.contactAddressesSharedCollection = contactAddresses));

    this.contactPersonService
      .query()
      .pipe(map((res: HttpResponse<IContactPerson[]>) => res.body ?? []))
      .pipe(
        map((contactPeople: IContactPerson[]) =>
          this.contactPersonService.addContactPersonToCollectionIfMissing(contactPeople, this.editForm.get('contactPerson')!.value)
        )
      )
      .subscribe((contactPeople: IContactPerson[]) => (this.contactPeopleSharedCollection = contactPeople));

    this.layoutService
      .query()
      .pipe(map((res: HttpResponse<ILayout[]>) => res.body ?? []))
      .pipe(map((layouts: ILayout[]) => this.layoutService.addLayoutToCollectionIfMissing(layouts, this.editForm.get('layout')!.value)))
      .subscribe((layouts: ILayout[]) => (this.layoutsSharedCollection = layouts));

    this.signatureService
      .query()
      .pipe(map((res: HttpResponse<ISignature[]>) => res.body ?? []))
      .pipe(
        map((signatures: ISignature[]) =>
          this.signatureService.addSignatureToCollectionIfMissing(signatures, this.editForm.get('layout')!.value)
        )
      )
      .subscribe((signatures: ISignature[]) => (this.signaturesSharedCollection = signatures));
  }

  protected createFromForm(): IOffer {
    return {
      ...new Offer(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      date: this.editForm.get(['date'])!.value,
      validUntil: this.editForm.get(['validUntil'])!.value,
      periodText: this.editForm.get(['periodText'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      total: this.editForm.get(['total'])!.value,
      vatIncluded: this.editForm.get(['vatIncluded'])!.value,
      discountRate: this.editForm.get(['discountRate'])!.value,
      discountType: this.editForm.get(['discountType'])!.value,
      acceptOnline: this.editForm.get(['acceptOnline'])!.value,
      acceptOnlineUrl: this.editForm.get(['acceptOnlineUrl'])!.value,
      acceptOnlineStatus: this.editForm.get(['acceptOnlineStatus'])!.value,
      language: this.editForm.get(['language'])!.value,
      pageAmount: this.editForm.get(['pageAmount'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      status: this.editForm.get(['status'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      contact: this.editForm.get(['contact'])!.value,
      contactAddress: this.editForm.get(['contactAddress'])!.value,
      contactPerson: this.editForm.get(['contactPerson'])!.value,
      contactPrePageAddress: this.editForm.get(['contactPrePageAddress'])!.value,
      layout: this.editForm.get(['layout'])!.value,
      layout: this.editForm.get(['layout'])!.value,
    };
  }
}
