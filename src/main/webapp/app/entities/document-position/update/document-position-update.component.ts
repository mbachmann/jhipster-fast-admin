import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentPosition, DocumentPosition } from '../document-position.model';
import { DocumentPositionService } from '../service/document-position.service';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { CatalogUnitService } from 'app/entities/catalog-unit/service/catalog-unit.service';
import { IDeliveryNote } from 'app/entities/delivery-note/delivery-note.model';
import { DeliveryNoteService } from 'app/entities/delivery-note/service/delivery-note.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';
import { IOffer } from 'app/entities/offer/offer.model';
import { OfferService } from 'app/entities/offer/service/offer.service';
import { IOrderConfirmation } from 'app/entities/order-confirmation/order-confirmation.model';
import { OrderConfirmationService } from 'app/entities/order-confirmation/service/order-confirmation.service';

@Component({
  selector: 'fa-document-position-update',
  templateUrl: './document-position-update.component.html',
})
export class DocumentPositionUpdateComponent implements OnInit {
  isSaving = false;

  catalogUnitsSharedCollection: ICatalogUnit[] = [];
  deliveryNotesSharedCollection: IDeliveryNote[] = [];
  invoicesSharedCollection: IInvoice[] = [];
  offersSharedCollection: IOffer[] = [];
  orderConfirmationsSharedCollection: IOrderConfirmation[] = [];

  editForm = this.fb.group({
    id: [],
    type: [],
    catalogType: [],
    number: [],
    name: [],
    description: [],
    price: [],
    vat: [],
    amount: [],
    discountRate: [],
    discountType: [],
    total: [],
    showOnlyTotal: [],
    unit: [],
    deliveryNote: [],
    invoice: [],
    offer: [],
    orderConfirmation: [],
  });

  constructor(
    protected documentPositionService: DocumentPositionService,
    protected catalogUnitService: CatalogUnitService,
    protected deliveryNoteService: DeliveryNoteService,
    protected invoiceService: InvoiceService,
    protected offerService: OfferService,
    protected orderConfirmationService: OrderConfirmationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentPosition }) => {
      this.updateForm(documentPosition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const documentPosition = this.createFromForm();
    if (documentPosition.id !== undefined) {
      this.subscribeToSaveResponse(this.documentPositionService.update(documentPosition));
    } else {
      this.subscribeToSaveResponse(this.documentPositionService.create(documentPosition));
    }
  }

  trackCatalogUnitById(index: number, item: ICatalogUnit): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentPosition>>): void {
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

  protected updateForm(documentPosition: IDocumentPosition): void {
    this.editForm.patchValue({
      id: documentPosition.id,
      type: documentPosition.type,
      catalogType: documentPosition.catalogType,
      number: documentPosition.number,
      name: documentPosition.name,
      description: documentPosition.description,
      price: documentPosition.price,
      vat: documentPosition.vat,
      amount: documentPosition.amount,
      discountRate: documentPosition.discountRate,
      discountType: documentPosition.discountType,
      total: documentPosition.total,
      showOnlyTotal: documentPosition.showOnlyTotal,
      unit: documentPosition.unit,
      deliveryNote: documentPosition.deliveryNote,
      invoice: documentPosition.invoice,
      offer: documentPosition.offer,
      orderConfirmation: documentPosition.orderConfirmation,
    });

    this.catalogUnitsSharedCollection = this.catalogUnitService.addCatalogUnitToCollectionIfMissing(
      this.catalogUnitsSharedCollection,
      documentPosition.unit
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      documentPosition.deliveryNote
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing(
      this.invoicesSharedCollection,
      documentPosition.invoice
    );
    this.offersSharedCollection = this.offerService.addOfferToCollectionIfMissing(this.offersSharedCollection, documentPosition.offer);
    this.orderConfirmationsSharedCollection = this.orderConfirmationService.addOrderConfirmationToCollectionIfMissing(
      this.orderConfirmationsSharedCollection,
      documentPosition.orderConfirmation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogUnitService
      .query()
      .pipe(map((res: HttpResponse<ICatalogUnit[]>) => res.body ?? []))
      .pipe(
        map((catalogUnits: ICatalogUnit[]) =>
          this.catalogUnitService.addCatalogUnitToCollectionIfMissing(catalogUnits, this.editForm.get('unit')!.value)
        )
      )
      .subscribe((catalogUnits: ICatalogUnit[]) => (this.catalogUnitsSharedCollection = catalogUnits));

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

  protected createFromForm(): IDocumentPosition {
    return {
      ...new DocumentPosition(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      catalogType: this.editForm.get(['catalogType'])!.value,
      number: this.editForm.get(['number'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      price: this.editForm.get(['price'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      discountRate: this.editForm.get(['discountRate'])!.value,
      discountType: this.editForm.get(['discountType'])!.value,
      total: this.editForm.get(['total'])!.value,
      showOnlyTotal: this.editForm.get(['showOnlyTotal'])!.value,
      unit: this.editForm.get(['unit'])!.value,
      deliveryNote: this.editForm.get(['deliveryNote'])!.value,
      invoice: this.editForm.get(['invoice'])!.value,
      offer: this.editForm.get(['offer'])!.value,
      orderConfirmation: this.editForm.get(['orderConfirmation'])!.value,
    };
  }
}
