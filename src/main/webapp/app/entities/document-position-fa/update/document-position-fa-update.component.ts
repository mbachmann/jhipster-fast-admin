import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDocumentPositionFa, DocumentPositionFa } from '../document-position-fa.model';
import { DocumentPositionFaService } from '../service/document-position-fa.service';
import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { CatalogUnitFaService } from 'app/entities/catalog-unit-fa/service/catalog-unit-fa.service';
import { IDeliveryNoteFa } from 'app/entities/delivery-note-fa/delivery-note-fa.model';
import { DeliveryNoteFaService } from 'app/entities/delivery-note-fa/service/delivery-note-fa.service';
import { IInvoiceFa } from 'app/entities/invoice-fa/invoice-fa.model';
import { InvoiceFaService } from 'app/entities/invoice-fa/service/invoice-fa.service';
import { IOfferFa } from 'app/entities/offer-fa/offer-fa.model';
import { OfferFaService } from 'app/entities/offer-fa/service/offer-fa.service';
import { IOrderConfirmationFa } from 'app/entities/order-confirmation-fa/order-confirmation-fa.model';
import { OrderConfirmationFaService } from 'app/entities/order-confirmation-fa/service/order-confirmation-fa.service';

@Component({
  selector: 'fa-document-position-fa-update',
  templateUrl: './document-position-fa-update.component.html',
})
export class DocumentPositionFaUpdateComponent implements OnInit {
  isSaving = false;

  catalogUnitsSharedCollection: ICatalogUnitFa[] = [];
  deliveryNotesSharedCollection: IDeliveryNoteFa[] = [];
  invoicesSharedCollection: IInvoiceFa[] = [];
  offersSharedCollection: IOfferFa[] = [];
  orderConfirmationsSharedCollection: IOrderConfirmationFa[] = [];

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
    protected documentPositionService: DocumentPositionFaService,
    protected catalogUnitService: CatalogUnitFaService,
    protected deliveryNoteService: DeliveryNoteFaService,
    protected invoiceService: InvoiceFaService,
    protected offerService: OfferFaService,
    protected orderConfirmationService: OrderConfirmationFaService,
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

  trackCatalogUnitFaById(index: number, item: ICatalogUnitFa): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDocumentPositionFa>>): void {
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

  protected updateForm(documentPosition: IDocumentPositionFa): void {
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

    this.catalogUnitsSharedCollection = this.catalogUnitService.addCatalogUnitFaToCollectionIfMissing(
      this.catalogUnitsSharedCollection,
      documentPosition.unit
    );
    this.deliveryNotesSharedCollection = this.deliveryNoteService.addDeliveryNoteFaToCollectionIfMissing(
      this.deliveryNotesSharedCollection,
      documentPosition.deliveryNote
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceFaToCollectionIfMissing(
      this.invoicesSharedCollection,
      documentPosition.invoice
    );
    this.offersSharedCollection = this.offerService.addOfferFaToCollectionIfMissing(this.offersSharedCollection, documentPosition.offer);
    this.orderConfirmationsSharedCollection = this.orderConfirmationService.addOrderConfirmationFaToCollectionIfMissing(
      this.orderConfirmationsSharedCollection,
      documentPosition.orderConfirmation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogUnitService
      .query()
      .pipe(map((res: HttpResponse<ICatalogUnitFa[]>) => res.body ?? []))
      .pipe(
        map((catalogUnits: ICatalogUnitFa[]) =>
          this.catalogUnitService.addCatalogUnitFaToCollectionIfMissing(catalogUnits, this.editForm.get('unit')!.value)
        )
      )
      .subscribe((catalogUnits: ICatalogUnitFa[]) => (this.catalogUnitsSharedCollection = catalogUnits));

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

  protected createFromForm(): IDocumentPositionFa {
    return {
      ...new DocumentPositionFa(),
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
