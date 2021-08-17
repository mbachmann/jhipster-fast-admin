import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDeliveryNote, DeliveryNote } from '../delivery-note.model';
import { DeliveryNoteService } from '../service/delivery-note.service';
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
  selector: 'fa-delivery-note-update',
  templateUrl: './delivery-note-update.component.html',
})
export class DeliveryNoteUpdateComponent implements OnInit {
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
    periodText: [],
    currency: [],
    total: [],
    vatIncluded: [],
    discountRate: [],
    discountType: [],
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
    signature: [],
  });

  constructor(
    protected deliveryNoteService: DeliveryNoteService,
    protected contactService: ContactService,
    protected contactAddressService: ContactAddressService,
    protected contactPersonService: ContactPersonService,
    protected layoutService: LayoutService,
    protected signatureService: SignatureService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deliveryNote }) => {
      if (deliveryNote.id === undefined) {
        const today = dayjs().startOf('day');
        deliveryNote.created = today;
      }

      this.updateForm(deliveryNote);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deliveryNote = this.createFromForm();
    if (deliveryNote.id !== undefined) {
      this.subscribeToSaveResponse(this.deliveryNoteService.update(deliveryNote));
    } else {
      this.subscribeToSaveResponse(this.deliveryNoteService.create(deliveryNote));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeliveryNote>>): void {
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

  protected updateForm(deliveryNote: IDeliveryNote): void {
    this.editForm.patchValue({
      id: deliveryNote.id,
      remoteId: deliveryNote.remoteId,
      number: deliveryNote.number,
      contactName: deliveryNote.contactName,
      date: deliveryNote.date,
      periodText: deliveryNote.periodText,
      currency: deliveryNote.currency,
      total: deliveryNote.total,
      vatIncluded: deliveryNote.vatIncluded,
      discountRate: deliveryNote.discountRate,
      discountType: deliveryNote.discountType,
      language: deliveryNote.language,
      pageAmount: deliveryNote.pageAmount,
      notes: deliveryNote.notes,
      status: deliveryNote.status,
      created: deliveryNote.created ? deliveryNote.created.format(DATE_TIME_FORMAT) : null,
      contact: deliveryNote.contact,
      contactAddress: deliveryNote.contactAddress,
      contactPerson: deliveryNote.contactPerson,
      contactPrePageAddress: deliveryNote.contactPrePageAddress,
      layout: deliveryNote.layout,
      signature: deliveryNote.signature,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      deliveryNote.contact
    );
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      deliveryNote.contactAddress,
      deliveryNote.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      deliveryNote.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutToCollectionIfMissing(this.layoutsSharedCollection, deliveryNote.layout);
    this.signaturesSharedCollection = this.signatureService.addSignatureToCollectionIfMissing(
      this.signaturesSharedCollection,
      deliveryNote.signature
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
          this.signatureService.addSignatureToCollectionIfMissing(signatures, this.editForm.get('signature')!.value)
        )
      )
      .subscribe((signatures: ISignature[]) => (this.signaturesSharedCollection = signatures));
  }

  protected createFromForm(): IDeliveryNote {
    return {
      ...new DeliveryNote(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      date: this.editForm.get(['date'])!.value,
      periodText: this.editForm.get(['periodText'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      total: this.editForm.get(['total'])!.value,
      vatIncluded: this.editForm.get(['vatIncluded'])!.value,
      discountRate: this.editForm.get(['discountRate'])!.value,
      discountType: this.editForm.get(['discountType'])!.value,
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
      signature: this.editForm.get(['signature'])!.value,
    };
  }
}
