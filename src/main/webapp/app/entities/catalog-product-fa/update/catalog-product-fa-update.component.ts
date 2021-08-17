import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICatalogProductFa, CatalogProductFa } from '../catalog-product-fa.model';
import { CatalogProductFaService } from '../service/catalog-product-fa.service';
import { ICatalogCategoryFa } from 'app/entities/catalog-category-fa/catalog-category-fa.model';
import { CatalogCategoryFaService } from 'app/entities/catalog-category-fa/service/catalog-category-fa.service';
import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { CatalogUnitFaService } from 'app/entities/catalog-unit-fa/service/catalog-unit-fa.service';
import { IVatFa } from 'app/entities/vat-fa/vat-fa.model';
import { VatFaService } from 'app/entities/vat-fa/service/vat-fa.service';

@Component({
  selector: 'fa-catalog-product-fa-update',
  templateUrl: './catalog-product-fa-update.component.html',
})
export class CatalogProductFaUpdateComponent implements OnInit {
  isSaving = false;

  catalogCategoriesSharedCollection: ICatalogCategoryFa[] = [];
  catalogUnitsSharedCollection: ICatalogUnitFa[] = [];
  vatsSharedCollection: IVatFa[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    number: [null, []],
    name: [null, [Validators.required]],
    description: [],
    notes: [],
    categoryName: [],
    includingVat: [],
    vat: [],
    unitName: [],
    price: [],
    pricePurchase: [],
    inventoryAvailable: [],
    inventoryReserved: [],
    defaultAmount: [],
    created: [],
    inactiv: [],
    category: [],
    unit: [],
    vat: [],
  });

  constructor(
    protected catalogProductService: CatalogProductFaService,
    protected catalogCategoryService: CatalogCategoryFaService,
    protected catalogUnitService: CatalogUnitFaService,
    protected vatService: VatFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogProduct }) => {
      if (catalogProduct.id === undefined) {
        const today = dayjs().startOf('day');
        catalogProduct.created = today;
      }

      this.updateForm(catalogProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogProduct = this.createFromForm();
    if (catalogProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogProductService.update(catalogProduct));
    } else {
      this.subscribeToSaveResponse(this.catalogProductService.create(catalogProduct));
    }
  }

  trackCatalogCategoryFaById(index: number, item: ICatalogCategoryFa): number {
    return item.id!;
  }

  trackCatalogUnitFaById(index: number, item: ICatalogUnitFa): number {
    return item.id!;
  }

  trackVatFaById(index: number, item: IVatFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogProductFa>>): void {
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

  protected updateForm(catalogProduct: ICatalogProductFa): void {
    this.editForm.patchValue({
      id: catalogProduct.id,
      remoteId: catalogProduct.remoteId,
      number: catalogProduct.number,
      name: catalogProduct.name,
      description: catalogProduct.description,
      notes: catalogProduct.notes,
      categoryName: catalogProduct.categoryName,
      includingVat: catalogProduct.includingVat,
      vat: catalogProduct.vat,
      unitName: catalogProduct.unitName,
      price: catalogProduct.price,
      pricePurchase: catalogProduct.pricePurchase,
      inventoryAvailable: catalogProduct.inventoryAvailable,
      inventoryReserved: catalogProduct.inventoryReserved,
      defaultAmount: catalogProduct.defaultAmount,
      created: catalogProduct.created ? catalogProduct.created.format(DATE_TIME_FORMAT) : null,
      inactiv: catalogProduct.inactiv,
      category: catalogProduct.category,
      unit: catalogProduct.unit,
      vat: catalogProduct.vat,
    });

    this.catalogCategoriesSharedCollection = this.catalogCategoryService.addCatalogCategoryFaToCollectionIfMissing(
      this.catalogCategoriesSharedCollection,
      catalogProduct.category
    );
    this.catalogUnitsSharedCollection = this.catalogUnitService.addCatalogUnitFaToCollectionIfMissing(
      this.catalogUnitsSharedCollection,
      catalogProduct.unit
    );
    this.vatsSharedCollection = this.vatService.addVatFaToCollectionIfMissing(this.vatsSharedCollection, catalogProduct.vat);
  }

  protected loadRelationshipsOptions(): void {
    this.catalogCategoryService
      .query()
      .pipe(map((res: HttpResponse<ICatalogCategoryFa[]>) => res.body ?? []))
      .pipe(
        map((catalogCategories: ICatalogCategoryFa[]) =>
          this.catalogCategoryService.addCatalogCategoryFaToCollectionIfMissing(catalogCategories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((catalogCategories: ICatalogCategoryFa[]) => (this.catalogCategoriesSharedCollection = catalogCategories));

    this.catalogUnitService
      .query()
      .pipe(map((res: HttpResponse<ICatalogUnitFa[]>) => res.body ?? []))
      .pipe(
        map((catalogUnits: ICatalogUnitFa[]) =>
          this.catalogUnitService.addCatalogUnitFaToCollectionIfMissing(catalogUnits, this.editForm.get('unit')!.value)
        )
      )
      .subscribe((catalogUnits: ICatalogUnitFa[]) => (this.catalogUnitsSharedCollection = catalogUnits));

    this.vatService
      .query()
      .pipe(map((res: HttpResponse<IVatFa[]>) => res.body ?? []))
      .pipe(map((vats: IVatFa[]) => this.vatService.addVatFaToCollectionIfMissing(vats, this.editForm.get('vat')!.value)))
      .subscribe((vats: IVatFa[]) => (this.vatsSharedCollection = vats));
  }

  protected createFromForm(): ICatalogProductFa {
    return {
      ...new CatalogProductFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      number: this.editForm.get(['number'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      categoryName: this.editForm.get(['categoryName'])!.value,
      includingVat: this.editForm.get(['includingVat'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      unitName: this.editForm.get(['unitName'])!.value,
      price: this.editForm.get(['price'])!.value,
      pricePurchase: this.editForm.get(['pricePurchase'])!.value,
      inventoryAvailable: this.editForm.get(['inventoryAvailable'])!.value,
      inventoryReserved: this.editForm.get(['inventoryReserved'])!.value,
      defaultAmount: this.editForm.get(['defaultAmount'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      inactiv: this.editForm.get(['inactiv'])!.value,
      category: this.editForm.get(['category'])!.value,
      unit: this.editForm.get(['unit'])!.value,
      vat: this.editForm.get(['vat'])!.value,
    };
  }
}
