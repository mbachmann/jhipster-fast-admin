import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICatalogServiceFa, CatalogServiceFa } from '../catalog-service-fa.model';
import { CatalogServiceFaService } from '../service/catalog-service-fa.service';
import { ICatalogCategoryFa } from 'app/entities/catalog-category-fa/catalog-category-fa.model';
import { CatalogCategoryFaService } from 'app/entities/catalog-category-fa/service/catalog-category-fa.service';
import { ICatalogUnitFa } from 'app/entities/catalog-unit-fa/catalog-unit-fa.model';
import { CatalogUnitFaService } from 'app/entities/catalog-unit-fa/service/catalog-unit-fa.service';
import { IVatFa } from 'app/entities/vat-fa/vat-fa.model';
import { VatFaService } from 'app/entities/vat-fa/service/vat-fa.service';

@Component({
  selector: 'fa-catalog-service-fa-update',
  templateUrl: './catalog-service-fa-update.component.html',
})
export class CatalogServiceFaUpdateComponent implements OnInit {
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
    defaultAmount: [],
    created: [],
    inactiv: [],
    category: [],
    unit: [],
    vat: [],
  });

  constructor(
    protected catalogServiceService: CatalogServiceFaService,
    protected catalogCategoryService: CatalogCategoryFaService,
    protected catalogUnitService: CatalogUnitFaService,
    protected vatService: VatFaService,
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

  trackCatalogCategoryFaById(index: number, item: ICatalogCategoryFa): number {
    return item.id!;
  }

  trackCatalogUnitFaById(index: number, item: ICatalogUnitFa): number {
    return item.id!;
  }

  trackVatFaById(index: number, item: IVatFa): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogServiceFa>>): void {
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

  protected updateForm(catalogService: ICatalogServiceFa): void {
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
      vat: catalogService.vat,
    });

    this.catalogCategoriesSharedCollection = this.catalogCategoryService.addCatalogCategoryFaToCollectionIfMissing(
      this.catalogCategoriesSharedCollection,
      catalogService.category
    );
    this.catalogUnitsSharedCollection = this.catalogUnitService.addCatalogUnitFaToCollectionIfMissing(
      this.catalogUnitsSharedCollection,
      catalogService.unit
    );
    this.vatsSharedCollection = this.vatService.addVatFaToCollectionIfMissing(this.vatsSharedCollection, catalogService.vat);
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

  protected createFromForm(): ICatalogServiceFa {
    return {
      ...new CatalogServiceFa(),
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
      vat: this.editForm.get(['vat'])!.value,
    };
  }
}
