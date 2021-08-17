import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrderConfirmationFa, OrderConfirmationFa } from '../order-confirmation-fa.model';
import { OrderConfirmationFaService } from '../service/order-confirmation-fa.service';
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
  selector: 'fa-order-confirmation-fa-update',
  templateUrl: './order-confirmation-fa-update.component.html',
})
export class OrderConfirmationFaUpdateComponent implements OnInit {
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
    layout: [],
  });

  constructor(
    protected orderConfirmationService: OrderConfirmationFaService,
    protected contactService: ContactFaService,
    protected contactAddressService: ContactAddressFaService,
    protected contactPersonService: ContactPersonFaService,
    protected layoutService: LayoutFaService,
    protected signatureService: SignatureFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderConfirmation }) => {
      if (orderConfirmation.id === undefined) {
        const today = dayjs().startOf('day');
        orderConfirmation.created = today;
      }

      this.updateForm(orderConfirmation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderConfirmation = this.createFromForm();
    if (orderConfirmation.id !== undefined) {
      this.subscribeToSaveResponse(this.orderConfirmationService.update(orderConfirmation));
    } else {
      this.subscribeToSaveResponse(this.orderConfirmationService.create(orderConfirmation));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderConfirmationFa>>): void {
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

  protected updateForm(orderConfirmation: IOrderConfirmationFa): void {
    this.editForm.patchValue({
      id: orderConfirmation.id,
      remoteId: orderConfirmation.remoteId,
      number: orderConfirmation.number,
      contactName: orderConfirmation.contactName,
      date: orderConfirmation.date,
      periodText: orderConfirmation.periodText,
      currency: orderConfirmation.currency,
      total: orderConfirmation.total,
      vatIncluded: orderConfirmation.vatIncluded,
      discountRate: orderConfirmation.discountRate,
      discountType: orderConfirmation.discountType,
      language: orderConfirmation.language,
      pageAmount: orderConfirmation.pageAmount,
      notes: orderConfirmation.notes,
      status: orderConfirmation.status,
      created: orderConfirmation.created ? orderConfirmation.created.format(DATE_TIME_FORMAT) : null,
      contact: orderConfirmation.contact,
      contactAddress: orderConfirmation.contactAddress,
      contactPerson: orderConfirmation.contactPerson,
      contactPrePageAddress: orderConfirmation.contactPrePageAddress,
      layout: orderConfirmation.layout,
      layout: orderConfirmation.layout,
    });

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(
      this.contactsSharedCollection,
      orderConfirmation.contact
    );
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressFaToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      orderConfirmation.contactAddress,
      orderConfirmation.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonFaToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      orderConfirmation.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutFaToCollectionIfMissing(
      this.layoutsSharedCollection,
      orderConfirmation.layout
    );
    this.signaturesSharedCollection = this.signatureService.addSignatureFaToCollectionIfMissing(
      this.signaturesSharedCollection,
      orderConfirmation.layout
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

  protected createFromForm(): IOrderConfirmationFa {
    return {
      ...new OrderConfirmationFa(),
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
      layout: this.editForm.get(['layout'])!.value,
    };
  }
}
