import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOfferFa, OfferFa } from '../offer-fa.model';
import { OfferFaService } from '../service/offer-fa.service';
import { IContactFa } from 'app/entities/contact-fa/contact-fa.model';
import { ContactFaService } from 'app/entities/contact-fa/service/contact-fa.service';
import { IContactAddressFa } from 'app/entities/contact-address-fa/contact-address-fa.model';
import { ContactAddressFaService } from 'app/entities/contact-address-fa/service/contact-address-fa.service';
import { IContactPersonFa } from 'app/entities/contact-person-fa/contact-person-fa.model';
import { ContactPersonFaService } from 'app/entities/contact-person-fa/service/contact-person-fa.service';
import { ILayoutFa } from 'app/entities/layout-fa/layout-fa.model';
import { LayoutFaService } from 'app/entities/layout-fa/service/layout-fa.service';
import { ISignatureFa } from 'app/entities/signature-fa/signature-fa.model';
import { SignatureFaService } from 'app/entities/signature-fa/service/signature-fa.service';

@Component({
  selector: 'fa-offer-fa-update',
  templateUrl: './offer-fa-update.component.html',
})
export class OfferFaUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactFa[] = [];
  contactAddressesSharedCollection: IContactAddressFa[] = [];
  contactPeopleSharedCollection: IContactPersonFa[] = [];
  layoutsSharedCollection: ILayoutFa[] = [];
  signaturesSharedCollection: ISignatureFa[] = [];

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
    protected offerService: OfferFaService,
    protected contactService: ContactFaService,
    protected contactAddressService: ContactAddressFaService,
    protected contactPersonService: ContactPersonFaService,
    protected layoutService: LayoutFaService,
    protected signatureService: SignatureFaService,
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

  trackContactFaById(index: number, item: IContactFa): number {
    return item.id!;
  }

  trackContactAddressFaById(index: number, item: IContactAddressFa): number {
    return item.id!;
  }

  trackContactPersonFaById(index: number, item: IContactPersonFa): number {
    return item.id!;
  }

  trackLayoutFaById(index: number, item: ILayoutFa): number {
    return item.id!;
  }

  trackSignatureFaById(index: number, item: ISignatureFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOfferFa>>): void {
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

  protected updateForm(offer: IOfferFa): void {
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

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(this.contactsSharedCollection, offer.contact);
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressFaToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      offer.contactAddress,
      offer.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonFaToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      offer.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutFaToCollectionIfMissing(this.layoutsSharedCollection, offer.layout);
    this.signaturesSharedCollection = this.signatureService.addSignatureFaToCollectionIfMissing(
      this.signaturesSharedCollection,
      offer.layout
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

    this.contactAddressService
      .query()
      .pipe(map((res: HttpResponse<IContactAddressFa[]>) => res.body ?? []))
      .pipe(
        map((contactAddresses: IContactAddressFa[]) =>
          this.contactAddressService.addContactAddressFaToCollectionIfMissing(
            contactAddresses,
            this.editForm.get('contactAddress')!.value,
            this.editForm.get('contactPrePageAddress')!.value
          )
        )
      )
      .subscribe((contactAddresses: IContactAddressFa[]) => (this.contactAddressesSharedCollection = contactAddresses));

    this.contactPersonService
      .query()
      .pipe(map((res: HttpResponse<IContactPersonFa[]>) => res.body ?? []))
      .pipe(
        map((contactPeople: IContactPersonFa[]) =>
          this.contactPersonService.addContactPersonFaToCollectionIfMissing(contactPeople, this.editForm.get('contactPerson')!.value)
        )
      )
      .subscribe((contactPeople: IContactPersonFa[]) => (this.contactPeopleSharedCollection = contactPeople));

    this.layoutService
      .query()
      .pipe(map((res: HttpResponse<ILayoutFa[]>) => res.body ?? []))
      .pipe(map((layouts: ILayoutFa[]) => this.layoutService.addLayoutFaToCollectionIfMissing(layouts, this.editForm.get('layout')!.value)))
      .subscribe((layouts: ILayoutFa[]) => (this.layoutsSharedCollection = layouts));

    this.signatureService
      .query()
      .pipe(map((res: HttpResponse<ISignatureFa[]>) => res.body ?? []))
      .pipe(
        map((signatures: ISignatureFa[]) =>
          this.signatureService.addSignatureFaToCollectionIfMissing(signatures, this.editForm.get('layout')!.value)
        )
      )
      .subscribe((signatures: ISignatureFa[]) => (this.signaturesSharedCollection = signatures));
  }

  protected createFromForm(): IOfferFa {
    return {
      ...new OfferFa(),
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
