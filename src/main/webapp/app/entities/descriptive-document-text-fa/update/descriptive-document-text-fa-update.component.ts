import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDescriptiveDocumentTextFa, DescriptiveDocumentTextFa } from '../descriptive-document-text-fa.model';
import { DescriptiveDocumentTextFaService } from '../service/descriptive-document-text-fa.service';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { DeliveryNoteFaService } from 'app/entities/delivery-note-fa/service/delivery-note-fa.service';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { InvoiceFaService } from 'app/entities/invoice-fa/service/invoice-fa.service';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { OfferFaService } from 'app/entities/offer-fa/service/offer-fa.service';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { OrderConfirmationFaService } from 'app/entities/order-confirmation-fa/service/order-confirmation-fa.service';

@Component({
  selector: 'fa-descriptive-document-text-fa-update',
  templateUrl: './descriptive-document-text-fa-update.component.html',
})
export class DescriptiveDocumentTextFaUpdateComponent implements OnInit {
  isSaving = false;

  deliveryNotesSharedCollection: IDeliveryNoteFa[] = [];
  invoicesSharedCollection: IInvoiceFa[] = [];
  offersSharedCollection: IOfferFa[] = [];
  orderConfirmationsSharedCollection: IOrderConfirmationFa[] = [];

  editForm = this.fb.group({
    id: [],
    title: [],
    introduction: [],
    conditions: [],
    status: [],
    deliveryNote: [],
    invoice: [],
    offer: [],
    orderConfirmation: [],
  });

  constructor(
    protected descriptiveDocumentTextService: DescriptiveDocumentTextFaService,
    protected deliveryNoteService: DeliveryNoteFaService,
    protected invoiceService: InvoiceFaService,
    protected offerService: OfferFaService,
    protected orderConfirmationService: OrderConfirmationFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ descriptiveDocumentText }) => {
      this.updateForm(descriptiveDocumentText);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const descriptiveDocumentText = this.createFromForm();
    if (descriptiveDocumentText.id !== undefined) {
      this.subscribeToSaveResponse(this.descriptiveDocumentTextService.update(descriptiveDocumentText));
    } else {
      this.subscribeToSaveResponse(this.descriptiveDocumentTextService.create(descriptiveDocumentText));
    }
  }

  trackDeliveryNoteFaById(index: number, item: IDeliveryNoteFa): number {
    return item.id!;
  }

  trackInvoiceFaById(index: number, item: IInvoiceFa): number {
    return item.id!;
  }

  trackOfferFaById(index: number, item: IOfferFa): number {
    return item.id!;
  }

  trackOrderConfirmationFaById(index: number, item: IOrderConfirmationFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDescriptiveDocumentTextFa>>): void {
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

  protected updateForm(descriptiveDocumentText: IDescriptiveDocumentTextFa): void {
    this.editForm.patchValue({
      id: descriptiveDocumentText.id,
      title: descriptiveDocumentText.title,
      introduction: descriptiveDocumentText.introduction,
      conditions: descriptiveDocumentText.conditions,
      status: descriptiveDocumentText.status,
      deliveryNote: descriptiveDocumentText.deliveryNote,
      invoice: descriptiveDocumentText.invoice,
      offer: descriptiveDocumentText.offer,
      orderConfirmation: descriptiveDocumentText.orderConfirmation,
    });

    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      descriptiveDocumentText.deliveryNote
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceFaToCollectionIfMissing(
      this.invoicesSharedCollection,
      descriptiveDocumentText.invoice
    );
    this.offersSharedCollection = this.offerService.addOfferFaToCollectionIfMissing(
      this.offersSharedCollection,
      descriptiveDocumentText.offer
    );
    this.orderConfirmationsSharedCollection = this.orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing(
      this.orderConfirmationsSharedCollection,
      descriptiveDocumentText.orderConfirmation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deliveryNoteService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryNoteFa[]>) => res.body ?? []))
      .pipe(
        map((deliveryNotes: IDeliveryNoteFa[]) =>
          this.deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing(deliveryNotes, this.editForm.get('deliveryNote')!.value)
        )
      )
      .subscribe((deliveryNotes: IDeliveryNoteFa[]) => (this.deliveryNotesSharedCollection = deliveryNotes));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceFa[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoiceFa[]) =>
          this.invoiceService.addInvoiceFaToCollectionIfMissing(invoices, this.editForm.get('invoice')!.value)
        )
      )
      .subscribe((invoices: IInvoiceFa[]) => (this.invoicesSharedCollection = invoices));

    this.offerService
      .query()
      .pipe(map((res: HttpResponse<IOfferFa[]>) => res.body ?? []))
      .pipe(map((offers: IOfferFa[]) => this.offerService.addOfferFaToCollectionIfMissing(offers, this.editForm.get('offer')!.value)))
      .subscribe((offers: IOfferFa[]) => (this.offersSharedCollection = offers));

    this.orderConfirmationService
      .query()
      .pipe(map((res: HttpResponse<IOrderConfirmationFa[]>) => res.body ?? []))
      .pipe(
        map((orderConfirmations: IOrderConfirmationFa[]) =>
          this.orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing(
            orderConfirmations,
            this.editForm.get('orderConfirmation')!.value
          )
        )
      )
      .subscribe((orderConfirmations: IOrderConfirmationFa[]) => (this.orderConfirmationsSharedCollection = orderConfirmations));
  }

  protected createFromForm(): IDescriptiveDocumentTextFa {
    return {
      ...new DescriptiveDocumentTextFa(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      introduction: this.editForm.get(['introduction'])!.value,
      conditions: this.editForm.get(['conditions'])!.value,
      status: this.editForm.get(['status'])!.value,
      deliveryNote: this.editForm.get(['deliveryNote'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
      offer: this.editForm.get(['offer'])!.value,
      orderConfirmation: this.editForm.get(['orderConfirmation'])!.value,
    };
  }
}
