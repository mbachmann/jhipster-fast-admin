import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICatalogService, CatalogService } from '../catalog-service.model';
import { CatalogServiceService } from '../service/catalog-service.service';
import { ICatalogCategory } from 'app/entities/catalog-category/catalog-category.model';
import { CatalogCategoryService } from 'app/entities/catalog-category/service/catalog-category.service';
import { ICatalogUnit } from 'app/entities/catalog-unit/catalog-unit.model';
import { CatalogUnitService } from 'app/entities/catalog-unit/service/catalog-unit.service';
import { IValueAddedTax } from 'app/entities/value-added-tax/value-added-tax.model';
import { ValueAddedTaxService } from 'app/entities/value-added-tax/service/value-added-tax.service';

@Component({
  selector: 'fa-catalog-service-update',
  templateUrl: './catalog-service-update.component.html',
})
export class CatalogServiceUpdateComponent implements OnInit {
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
    defaultAmount: [],
    created: [],
    inactiv: [],
    category: [],
    unit: [],
    valueAddedTax: [],
  });

  constructor(
    protected catalogServiceService: CatalogServiceService,
    protected catalogCategoryService: CatalogCategoryService,
    protected catalogUnitService: CatalogUnitService,
    protected valueAddedTaxService: ValueAddedTaxService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogService }) => {
      if (catalogService.id === undefined) {
        const today = dayjs().startOf('day');
        catalogService.created = today;
      }

      this.updateForm(catalogService);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogService = this.createFromForm();
    if (catalogService.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogServiceService.update(catalogService));
    } else {
      this.subscribeToSaveResponse(this.catalogServiceService.create(catalogService));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogService>>): void {
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

  protected updateForm(catalogService: ICatalogService): void {
    this.editForm.patchValue({
      id: catalogService.id,
      remoteId: catalogService.remoteId,
      number: catalogService.number,
      name: catalogService.name,
      description: catalogService.description,
      notes: catalogService.notes,
      categoryName: catalogService.categoryName,
      includingVat: catalogService.includingVat,
      vat: catalogService.vat,
      unitName: catalogService.unitName,
      price: catalogService.price,
      defaultAmount: catalogService.defaultAmount,
      created: catalogService.created ? catalogService.created.format(DATE_TIME_FORMAT) : null,
      inactiv: catalogService.inactiv,
      category: catalogService.category,
      unit: catalogService.unit,
      valueAddedTax: catalogService.valueAddedTax,
    });

    this.catalogCategoriesSharedCollection = this.catalogCategoryService.addCatalogCategoryToCollectionIfMissing(
      this.catalogCategoriesSharedCollection,
      catalogService.category
    );
    this.catalogUnitsSharedCollection = this.catalogUnitService.addCatalogUnitToCollectionIfMissing(
      this.catalogUnitsSharedCollection,
      catalogService.unit
    );
    this.valueAddedTaxesSharedCollection = this.valueAddedTaxService.addValueAddedTaxToCollectionIfMissing(
      this.valueAddedTaxesSharedCollection,
      catalogService.valueAddedTax
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

  protected createFromForm(): ICatalogService {
    return {
      ...new CatalogService(),
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
      defaultAmount: this.editForm.get(['defaultAmount'])!.value,
      created: this.editForm.get(['created'])!.value ? dayjs(this.editForm.get(['created'])!.value, DATE_TIME_FORMAT) : undefined,
      inactiv: this.editForm.get(['inactiv'])!.value,
      category: this.editForm.get(['category'])!.value,
      unit: this.editForm.get(['unit'])!.value,
      valueAddedTax: this.editForm.get(['valueAddedTax'])!.value,
    };
  }
}
