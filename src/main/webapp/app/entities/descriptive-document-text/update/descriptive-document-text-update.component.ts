import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDescriptiveDocumentText, DescriptiveDocumentText } from '../descriptive-document-text.model';
import { DescriptiveDocumentTextService } from '../service/descriptive-document-text.service';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { OrderConfirmationService } from 'app/entities/order-confirmation/service/order-confirmation.service';

@Component({
  selector: 'fa-descriptive-document-text-update',
  templateUrl: './descriptive-document-text-update.component.html',
})
export class DescriptiveDocumentTextUpdateComponent implements OnInit {
  isSaving = false;

  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  invoicesSharedCollection: IInvoice[] = [];
  offersSharedCollection: IOffer[] = [];
  orderConfirmationsSharedCollection: IOrderConfirmation[] = [];

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
    protected descriptiveDocumentTextService: DescriptiveDocumentTextService,
    protected deliveryNoteService: DeliveryNoteService,
    protected invoiceService: InvoiceService,
    protected offerService: OfferService,
    protected orderConfirmationService: OrderConfirmationService,
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

  trackDeliveryNoteById(index: number, item: IDeliveryNote): number {
    return item.id!;
  }

  trackInvoiceById(index: number, item: IInvoice): number {
    return item.id!;
  }

  trackOfferById(index: number, item: IOffer): number {
    return item.id!;
  }

  trackOrderConfirmationById(index: number, item: IOrderConfirmation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDescriptiveDocumentText>>): void {
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

  protected updateForm(descriptiveDocumentText: IDescriptiveDocumentText): void {
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

    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      descriptiveDocumentText.deliveryNote
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing(
      this.invoicesSharedCollection,
      descriptiveDocumentText.invoice
    );
    this.offersSharedCollection = this.offerService.addOfferToCollectionIfMissing(
      this.offersSharedCollection,
      descriptiveDocumentText.offer
    );
    this.orderConfirmationsSharedCollection = this.orderConfirmationService.addOrderConfirmationToCollectionIfMissing(
      this.orderConfirmationsSharedCollection,
      descriptiveDocumentText.orderConfirmation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.deliveryNoteService
      .query()
      .pipe(map((res: HttpResponse<IDeliveryNote[]>) => res.body ?? []))
      .pipe(
        map((deliveryNotes: IDeliveryNote[]) =>
          this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(deliveryNotes, this.editForm.get('deliveryNote')!.value)
        )
      )
      .subscribe((deliveryNotes: IDeliveryNote[]) => (this.deliveryNotesSharedCollection = deliveryNotes));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing(invoices, this.editForm.get('invoice')!.value))
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));

    this.offerService
      .query()
      .pipe(map((res: HttpResponse<IOffer[]>) => res.body ?? []))
      .pipe(map((offers: IOffer[]) => this.offerService.addOfferToCollectionIfMissing(offers, this.editForm.get('offer')!.value)))
      .subscribe((offers: IOffer[]) => (this.offersSharedCollection = offers));

    this.orderConfirmationService
      .query()
      .pipe(map((res: HttpResponse<IOrderConfirmation[]>) => res.body ?? []))
      .pipe(
        map((orderConfirmations: IOrderConfirmation[]) =>
          this.orderConfirmationService.addOrderConfirmationToCollectionIfMissing(
            orderConfirmations,
            this.editForm.get('orderConfirmation')!.value
          )
        )
      )
      .subscribe((orderConfirmations: IOrderConfirmation[]) => (this.orderConfirmationsSharedCollection = orderConfirmations));
  }

  protected createFromForm(): IDescriptiveDocumentText {
    return {
      ...new DescriptiveDocumentText(),
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
