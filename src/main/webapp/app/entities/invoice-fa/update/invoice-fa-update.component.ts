import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IInvoiceFa, InvoiceFa } from '../invoice-fa.model';
import { InvoiceFaService } from '../service/invoice-fa.service';
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
import { IBankAccountFa } from 'app/entities/bank-account-fa/bank-account-fa.model';
import { BankAccountFaService } from 'app/entities/bank-account-fa/service/bank-account-fa.service';
import { IIsrFa } from 'app/entities/isr-fa/isr-fa.model';
import { IsrFaService } from 'app/entities/isr-fa/service/isr-fa.service';

@Component({
  selector: 'fa-invoice-fa-update',
  templateUrl: './invoice-fa-update.component.html',
})
export class InvoiceFaUpdateComponent implements OnInit {
  isSaving = false;

  contactsSharedCollection: IContactFa[] = [];
  contactAddressesSharedCollection: IContactAddressFa[] = [];
  contactPeopleSharedCollection: IContactPersonFa[] = [];
  layoutsSharedCollection: ILayoutFa[] = [];
  signaturesSharedCollection: ISignatureFa[] = [];
  bankAccountsSharedCollection: IBankAccountFa[] = [];
  isrsSharedCollection: IIsrFa[] = [];

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
    protected invoiceService: InvoiceFaService,
    protected contactService: ContactFaService,
    protected contactAddressService: ContactAddressFaService,
    protected contactPersonService: ContactPersonFaService,
    protected layoutService: LayoutFaService,
    protected signatureService: SignatureFaService,
    protected bankAccountService: BankAccountFaService,
    protected isrService: IsrFaService,
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

  trackBankAccountFaById(index: number, item: IBankAccountFa): number {
    return item.id!;
  }

  trackIsrFaById(index: number, item: IIsrFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceFa>>): void {
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

  protected updateForm(invoice: IInvoiceFa): void {
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

    this.contactsSharedCollection = this.contactService.addContactFaToCollectionIfMissing(this.contactsSharedCollection, invoice.contact);
    this.contactAddressesSharedCollection = this.contactAddressService.addContactAddressFaToCollectionIfMissing(
      this.contactAddressesSharedCollection,
      invoice.contactAddress,
      invoice.contactPrePageAddress
    );
    this.contactPeopleSharedCollection = this.contactPersonService.addContactPersonFaToCollectionIfMissing(
      this.contactPeopleSharedCollection,
      invoice.contactPerson
    );
    this.layoutsSharedCollection = this.layoutService.addLayoutFaToCollectionIfMissing(this.layoutsSharedCollection, invoice.layout);
    this.signaturesSharedCollection = this.signatureService.addSignatureFaToCollectionIfMissing(
      this.signaturesSharedCollection,
      invoice.layout
    );
    this.bankAccountsSharedCollection = this.bankAccountService.addBankAccountFaToCollectionIfMissing(
      this.bankAccountsSharedCollection,
      invoice.bankAccount
    );
    this.isrsSharedCollection = this.isrService.addIsrFaToCollectionIfMissing(this.isrsSharedCollection, invoice.isr);
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

    this.bankAccountService
      .query()
      .pipe(map((res: HttpResponse<IBankAccountFa[]>) => res.body ?? []))
      .pipe(
        map((bankAccounts: IBankAccountFa[]) =>
          this.bankAccountService.addBankAccountFaToCollectionIfMissing(bankAccounts, this.editForm.get('bankAccount')!.value)
        )
      )
      .subscribe((bankAccounts: IBankAccountFa[]) => (this.bankAccountsSharedCollection = bankAccounts));

    this.isrService
      .query()
      .pipe(map((res: HttpResponse<IIsrFa[]>) => res.body ?? []))
      .pipe(map((isrs: IIsrFa[]) => this.isrService.addIsrFaToCollectionIfMissing(isrs, this.editForm.get('isr')!.value)))
      .subscribe((isrs: IIsrFa[]) => (this.isrsSharedCollection = isrs));
  }

  protected createFromForm(): IInvoiceFa {
    return {
      ...new InvoiceFa(),
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
