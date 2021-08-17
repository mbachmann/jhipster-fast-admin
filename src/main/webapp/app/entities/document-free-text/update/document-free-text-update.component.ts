import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentFreeText, DocumentFreeText } from '../document-free-text.model';
import { DocumentFreeTextService } from '../service/document-free-text.service';
import { IDocumentLetter } from 'app/entities/document-letter/document-letter.model';
import { DocumentLetterService } from 'app/entities/document-letter/service/document-letter.service';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { OrderConfirmationService } from 'app/entities/order-confirmation/service/order-confirmation.service';

@Component({
  selector: 'fa-document-free-text-update',
  templateUrl: './document-free-text-update.component.html',
})
export class DocumentFreeTextUpdateComponent implements OnInit {
  isSaving = false;

  documentLettersSharedCollection: IDocumentLetter[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  invoicesSharedCollection: IInvoice[] = [];
  offersSharedCollection: IOffer[] = [];
  orderConfirmationsSharedCollection: IOrderConfirmation[] = [];

  editForm = this.fb.group({
    id: [],
    text: [],
    fontSize: [],
    positionX: [],
    positionY: [],
    pageNo: [],
    documentLetter: [],
    deliveryNote: [],
    invoice: [],
    offer: [],
    orderConfirmation: [],
  });

  constructor(
    protected documentFreeTextService: DocumentFreeTextService,
    protected documentLetterService: DocumentLetterService,
    protected deliveryNoteService: DeliveryNoteService,
    protected invoiceService: InvoiceService,
    protected offerService: OfferService,
    protected orderConfirmationService: OrderConfirmationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentFreeText }) => {
      this.updateForm(documentFreeText);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentFreeText = this.createFromForm();
    if (documentFreeText.id !== undefined) {
      this.subscribeToSaveResponse(this.documentFreeTextService.update(documentFreeText));
    } else {
      this.subscribeToSaveResponse(this.documentFreeTextService.create(documentFreeText));
    }
  }

  trackDocumentLetterById(index: number, item: IDocumentLetter): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentFreeText>>): void {
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

  protected updateForm(documentFreeText: IDocumentFreeText): void {
    this.editForm.patchValue({
      id: documentFreeText.id,
      text: documentFreeText.text,
      fontSize: documentFreeText.fontSize,
      positionX: documentFreeText.positionX,
      positionY: documentFreeText.positionY,
      pageNo: documentFreeText.pageNo,
      documentLetter: documentFreeText.documentLetter,
      deliveryNote: documentFreeText.deliveryNote,
      invoice: documentFreeText.invoice,
      offer: documentFreeText.offer,
      orderConfirmation: documentFreeText.orderConfirmation,
    });

    this.documentLettersSharedCollection = this.documentLetterService.addDocumentLetterToCollectionIfMissing(
      this.documentLettersSharedCollection,
      documentFreeText.documentLetter
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      documentFreeText.deliveryNote
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing(
      this.invoicesSharedCollection,
      documentFreeText.invoice
    );
    this.offersSharedCollection = this.offerService.addOfferToCollectionIfMissing(this.offersSharedCollection, documentFreeText.offer);
    this.orderConfirmationsSharedCollection = this.orderConfirmationService.addOrderConfirmationToCollectionIfMissing(
      this.orderConfirmationsSharedCollection,
      documentFreeText.orderConfirmation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentLetterService
      .query()
      .pipe(map((res: HttpResponse<IDocumentLetter[]>) => res.body ?? []))
      .pipe(
        map((documentLetters: IDocumentLetter[]) =>
          this.documentLetterService.addDocumentLetterToCollectionIfMissing(documentLetters, this.editForm.get('documentLetter')!.value)
        )
      )
      .subscribe((documentLetters: IDocumentLetter[]) => (this.documentLettersSharedCollection = documentLetters));

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

  protected createFromForm(): IDocumentFreeText {
    return {
      ...new DocumentFreeText(),
      id: this.editForm.get(['id'])!.value,
      text: this.editForm.get(['text'])!.value,
      fontSize: this.editForm.get(['fontSize'])!.value,
      positionX: this.editForm.get(['positionX'])!.value,
      positionY: this.editForm.get(['positionY'])!.value,
      pageNo: this.editForm.get(['pageNo'])!.value,
      documentLetter: this.editForm.get(['documentLetter'])!.value,
      deliveryNote: this.editForm.get(['deliveryNote'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
      offer: this.editForm.get(['offer'])!.value,
      orderConfirmation: this.editForm.get(['orderConfirmation'])!.value,
    };
  }
}
