import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvoice, Invoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
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
import { IBankAccount } from 'app/entities/bank-account/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account/service/bank-account.service';
import { IIsr } from 'app/entities/isr/isr.model';
import { IsrService } from 'app/entities/isr/service/isr.service';

@Component({
  selector: 'fa-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContact[] = [];
  contactAddressesSharedCollection: IContactAddress[] = [];
  contactPeopleSharedCollection: IContactPerson[] = [];
  layoutsSharedCollection: ILayout[] = [];
  signaturesSharedCollection: ISignature[] = [];
  bankAccountsSharedCollection: IBankAccount[] = [];
  isrsSharedCollection: IIsr[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [null, []],
    contactName: [],
    date: [],
    due: [],
    periodFrom: [],
    periodTo: [],
    periodText: [],
    currency: [],
    total: [],
    vatIncluded: [],
    discountRate: [],
    discountType: [],
    cashDiscountRate: [],
    cashDiscountDate: [],
    totalPaid: [],
    paidDate: [],
    isrPosition: [],
    isrReferenceNumber: [],
    paymentLinkPaypal: [],
    paymentLinkPaypalUrl: [],
    paymentLinkPostfinance: [],
    paymentLinkPostfinanceUrl: [],
    paymentLinkPayrexx: [],
    paymentLinkPayrexxUrl: [],
    paymentLinkSmartcommerce: [],
    paymentLinkSmartcommerceUrl: [],
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
    bankAccount: [],
    isr: [],
  });

  constructor(
    protected invoiceService: InvoiceService,
    protected contactService: ContactService,
    protected contactAddressService: ContactAddressService,
    protected contactPersonService: ContactPersonService,
    protected layoutService: LayoutService,
    protected signatureService: SignatureService,
    protected bankAccountService: BankAccountService,
    protected isrService: IsrService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      if (invoice.id === undefined) {
        const today = dayjs().startOf('day');
        invoice.created = today;
      }

      this.updateForm(invoice);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.createFromForm();
    if (invoice.id !== undefined) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
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

  trackBankAccountById(index: number, item: IBankAccount): number {
    return item.id!;
  }

  trackIsrById(index: number, item: IIsr): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.editForm.patchValue({
      id: invoice.id,
      remoteId: invoice.remoteId,
      number: invoice.number,
      contactName: invoice.contactName,
      date: invoice.date,
      due: invoice.due,
      periodFrom: invoice.periodFrom,
      periodTo: invoice.periodTo,
      periodText: invoice.periodText,
      currency: invoice.currency,
      total: invoice.total,
      vatIncluded: invoice.vatIncluded,
      discountRate: invoice.discountRate,
      discountType: invoice.discountType,
      cashDiscountRate: invoice.cashDiscountRate,
      cashDiscountDate: invoice.cashDiscountDate,
      totalPaid: invoice.totalPaid,
      paidDate: invoice.paidDate,
      isrPosition: invoice.isrPosition,
      isrReferenceNumber: invoice.isrReferenceNumber,
      paymentLinkPaypal: invoice.paymentLinkPaypal,
      paymentLinkPaypalUrl: invoice.paymentLinkPaypalUrl,
      paymentLinkPostfinance: invoice.paymentLinkPostfinance,
      paymentLinkPostfinanceUrl: invoice.paymentLinkPostfinanceUrl,
      paymentLinkPayrexx: invoice.paymentLinkPayrexx,
      paymentLinkPayrexxUrl: invoice.paymentLinkPayrexxUrl,
      paymentLinkSmartcommerce: invoice.paymentLinkSmartcommerce,
      paymentLinkSmartcommerceUrl: invoice.paymentLinkSmartcommerceUrl,
      language: invoice.language,
      pageAmount: invoice.pageAmount,
      notes: invoice.notes,
      status: invoice.status,
      created: invoice.created ? invoice.created.format(DATE_TIME_FORMAT) : null,
      contact: invoice.contact,
      contactAddress: invoice.contactAddress,
      contactPerson: invoice.contactPerson,
      contactPrePageAddress: invoice.contactPrePageAddress,
      layout: invoice.layout,
      layout: invoice.layout,
      bankAccount: invoice.bankAccount,
      isr: invoice.isr,
    });

    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(this.contactsSharedCollection, invoice.contact);
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      invoice.contactAddress,
      invoice.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      invoice.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutToCollectionIfMissing(this.layoutsSharedCollection, invoice.layout);
    this.signaturesSharedCollection = this.signatureService.addSignatureToCollectionIfMissing(
      this.signaturesSharedCollection,
      invoice.layout
    );
    this.bankAccountsSharedCollection = this.bankAccountService.addBankAccountToCollectionIfMissing(
      this.bankAccountsSharedCollection,
      invoice.bankAccount
    );
    this.isrsSharedCollection = this.isrService.addIsrToCollectionIfMissing(this.isrsSharedCollection, invoice.isr);
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

    this.bankAccountService
      .query()
      .pipe(map((res: HttpResponse<IBankAccount[]>) => res.body ?? []))
      .pipe(
        map((bankAccounts: IBankAccount[]) =>
          this.bankAccountService.addBankAccountToCollectionIfMissing(bankAccounts, this.editForm.get('bankAccount')!.value)
        )
      )
      .subscribe((bankAccounts: IBankAccount[]) => (this.bankAccountsSharedCollection = bankAccounts));

    this.isrService
      .query()
      .pipe(map((res: HttpResponse<IIsr[]>) => res.body ?? []))
      .pipe(map((isrs: IIsr[]) => this.isrService.addIsrToCollectionIfMissing(isrs, this.editForm.get('isr')!.value)))
      .subscribe((isrs: IIsr[]) => (this.isrsSharedCollection = isrs));
  }

  protected createFromForm(): IInvoice {
    return {
      ...new Invoice(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      date: this.editForm.get(['date'])!.value,
      due: this.editForm.get(['due'])!.value,
      periodFrom: this.editForm.get(['periodFrom'])!.value,
      periodTo: this.editForm.get(['periodTo'])!.value,
      periodText: this.editForm.get(['periodText'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      total: this.editForm.get(['total'])!.value,
      vatIncluded: this.editForm.get(['vatIncluded'])!.value,
      discountRate: this.editForm.get(['discountRate'])!.value,
      discountType: this.editForm.get(['discountType'])!.value,
      cashDiscountRate: this.editForm.get(['cashDiscountRate'])!.value,
      cashDiscountDate: this.editForm.get(['cashDiscountDate'])!.value,
      totalPaid: this.editForm.get(['totalPaid'])!.value,
      paidDate: this.editForm.get(['paidDate'])!.value,
      isrPosition: this.editForm.get(['isrPosition'])!.value,
      isrReferenceNumber: this.editForm.get(['isrReferenceNumber'])!.value,
      paymentLinkPaypal: this.editForm.get(['paymentLinkPaypal'])!.value,
      paymentLinkPaypalUrl: this.editForm.get(['paymentLinkPaypalUrl'])!.value,
      paymentLinkPostfinance: this.editForm.get(['paymentLinkPostfinance'])!.value,
      paymentLinkPostfinanceUrl: this.editForm.get(['paymentLinkPostfinanceUrl'])!.value,
      paymentLinkPayrexx: this.editForm.get(['paymentLinkPayrexx'])!.value,
      paymentLinkPayrexxUrl: this.editForm.get(['paymentLinkPayrexxUrl'])!.value,
      paymentLinkSmartcommerce: this.editForm.get(['paymentLinkSmartcommerce'])!.value,
      paymentLinkSmartcommerceUrl: this.editForm.get(['paymentLinkSmartcommerceUrl'])!.value,
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
      bankAccount: this.editForm.get(['bankAccount'])!.value,
      isr: this.editForm.get(['isr'])!.value,
    };
  }
}
