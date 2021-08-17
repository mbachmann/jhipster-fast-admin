import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentFreeTextFa, DocumentFreeTextFa } from '../document-free-text-fa.model';
import { DocumentFreeTextFaService } from '../service/document-free-text-fa.service';
import { IDocumentLetterFa } from 'app/entities/document-letter-fa/document-letter-fa.model';
import { DocumentLetterFaService } from 'app/entities/document-letter-fa/service/document-letter-fa.service';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { DeliveryNoteFaService } from 'app/entities/delivery-note-fa/service/delivery-note-fa.service';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { InvoiceFaService } from 'app/entities/invoice-fa/service/invoice-fa.service';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { OfferFaService } from 'app/entities/offer-fa/service/offer-fa.service';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { OrderConfirmationFaService } from 'app/entities/order-confirmation-fa/service/order-confirmation-fa.service';

@Component({
  selector: 'fa-document-free-text-fa-update',
  templateUrl: './document-free-text-fa-update.component.html',
})
export class DocumentFreeTextFaUpdateComponent implements OnInit {
  isSaving = false;

  documentLettersSharedCollection: IDocumentLetterFa[] = [];
  deliveryNotesSharedCollection: IDeliveryNoteFa[] = [];
  invoicesSharedCollection: IInvoiceFa[] = [];
  offersSharedCollection: IOfferFa[] = [];
  orderConfirmationsSharedCollection: IOrderConfirmationFa[] = [];

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
    protected documentFreeTextService: DocumentFreeTextFaService,
    protected documentLetterService: DocumentLetterFaService,
    protected deliveryNoteService: DeliveryNoteFaService,
    protected invoiceService: InvoiceFaService,
    protected offerService: OfferFaService,
    protected orderConfirmationService: OrderConfirmationFaService,
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

  trackDocumentLetterFaById(index: number, item: IDocumentLetterFa): number {
    return item.id!;
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentFreeTextFa>>): void {
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

  protected updateForm(documentFreeText: IDocumentFreeTextFa): void {
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

    this.documentLettersSharedCollection = this.documentLetterService.addDocumentLetterFaToCollectionIfMissing(
      this.documentLettersSharedCollection,
      documentFreeText.documentLetter
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      documentFreeText.deliveryNote
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceFaToCollectionIfMissing(
      this.invoicesSharedCollection,
      documentFreeText.invoice
    );
    this.offersSharedCollection = this.offerService.addOfferFaToCollectionIfMissing(this.offersSharedCollection, documentFreeText.offer);
    this.orderConfirmationsSharedCollection = this.orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing(
      this.orderConfirmationsSharedCollection,
      documentFreeText.orderConfirmation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentLetterService
      .query()
      .pipe(map((res: HttpResponse<IDocumentLetterFa[]>) => res.body ?? []))
      .pipe(
        map((documentLetters: IDocumentLetterFa[]) =>
          this.documentLetterService.addDocumentLetterFaToCollectionIfMissing(documentLetters, this.editForm.get('documentLetter')!.value)
        )
      )
      .subscribe((documentLetters: IDocumentLetterFa[]) => (this.documentLettersSharedCollection = documentLetters));

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

  protected createFromForm(): IDocumentFreeTextFa {
    return {
      ...new DocumentFreeTextFa(),
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
