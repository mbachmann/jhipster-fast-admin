import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICatalogProduct, CatalogProduct } from '../catalog-product.model';
import { CatalogProductService } from '../service/catalog-product.service';
import { ICatalogCategory } from 'app/entities/catalog-category/catalog-category.model';
import { CatalogCategoryService } from 'app/entities/catalog-category/service/catalog-category.service';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { CatalogUnitService } from 'app/entities/catalog-unit/service/catalog-unit.service';
import { IValueAddedTax } from 'app/entities/value-added-tax/value-added-tax.model';
import { ValueAddedTaxService } from 'app/entities/value-added-tax/service/value-added-tax.service';

@Component({
  selector: 'fa-catalog-product-update',
  templateUrl: './catalog-product-update.component.html',
})
export class CatalogProductUpdateComponent implements OnInit {
  isSaving = false;

  catalogCategoriesSharedCollection: ICatalogCategory[] = [];
  catalogUnitsSharedCollection: ICatalogUnit[] = [];
  valueAddedTaxesSharedCollection: IValueAddedTax[] = [];

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
    valueAddedTax: [],
  });

  constructor(
    protected catalogProductService: CatalogProductService,
    protected catalogCategoryService: CatalogCategoryService,
    protected catalogUnitService: CatalogUnitService,
    protected valueAddedTaxService: ValueAddedTaxService,
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

  trackCatalogCategoryById(index: number, item: ICatalogCategory): number {
    return item.id!;
  }

  trackCatalogUnitById(index: number, item: ICatalogUnit): number {
    return item.id!;
  }

  trackValueAddedTaxById(index: number, item: IValueAddedTax): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogProduct>>): void {
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

  protected updateForm(catalogProduct: ICatalogProduct): void {
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
      valueAddedTax: catalogProduct.valueAddedTax,
    });

    this.catalogCategoriesSharedCollection = this.catalogCategoryService.addCatalogCategoryToCollectionIfMissing(
      this.catalogCategoriesSharedCollection,
      catalogProduct.category
    );
    this.catalogUnitsSharedCollection = this.catalogUnitService.addCatalogUnitToCollectionIfMissing(
      this.catalogUnitsSharedCollection,
      catalogProduct.unit
    );
    this.valueAddedTaxesSharedCollection = this.valueAddedTaxService.addValueAddedTaxToCollectionIfMissing(
      this.valueAddedTaxesSharedCollection,
      catalogProduct.valueAddedTax
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogCategoryService
      .query()
      .pipe(map((res: HttpResponse<ICatalogCategory[]>) => res.body ?? []))
      .pipe(
        map((catalogCategories: ICatalogCategory[]) =>
          this.catalogCategoryService.addCatalogCategoryToCollectionIfMissing(catalogCategories, this.editForm.get('category')!.value)
        )
      )
      .subscribe((catalogCategories: ICatalogCategory[]) => (this.catalogCategoriesSharedCollection = catalogCategories));

    this.catalogUnitService
      .query()
      .pipe(map((res: HttpResponse<ICatalogUnit[]>) => res.body ?? []))
      .pipe(
        map((catalogUnits: ICatalogUnit[]) =>
          this.catalogUnitService.addCatalogUnitToCollectionIfMissing(catalogUnits, this.editForm.get('unit')!.value)
        )
      )
      .subscribe((catalogUnits: ICatalogUnit[]) => (this.catalogUnitsSharedCollection = catalogUnits));

    this.valueAddedTaxService
      .query()
      .pipe(map((res: HttpResponse<IValueAddedTax[]>) => res.body ?? []))
      .pipe(
        map((valueAddedTaxes: IValueAddedTax[]) =>
          this.valueAddedTaxService.addValueAddedTaxToCollectionIfMissing(valueAddedTaxes, this.editForm.get('valueAddedTax')!.value)
        )
      )
      .subscribe((valueAddedTaxes: IValueAddedTax[]) => (this.valueAddedTaxesSharedCollection = valueAddedTaxes));
  }

  protected createFromForm(): ICatalogProduct {
    return {
      ...new CatalogProduct(),
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
      valueAddedTax: this.editForm.get(['valueAddedTax'])!.value,
    };
  }
}
