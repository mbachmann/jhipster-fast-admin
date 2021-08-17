import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICatalogCategoryFa, CatalogCategoryFa } from '../catalog-category-fa.model';
import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';

@Component({
  selector: 'fa-catalog-category-fa-update',
  templateUrl: './catalog-category-fa-update.component.html',
})
export class CatalogCategoryFaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    name: [null, [Validators.required]],
    accountingAccountNumber: [],
    usage: [],
    inactiv: [],
  });

  constructor(
    protected catalogCategoryService: CatalogCategoryFaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ catalogCategory }) => {
      this.updateForm(catalogCategory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const catalogCategory = this.createFromForm();
    if (catalogCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.catalogCategoryService.update(catalogCategory));
    } else {
      this.subscribeToSaveResponse(this.catalogCategoryService.create(catalogCategory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICatalogCategoryFa>>): void {
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

  protected updateForm(catalogCategory: ICatalogCategoryFa): void {
    this.editForm.patchValue({
      id: catalogCategory.id,
      remoteId: catalogCategory.remoteId,
      name: catalogCategory.name,
      accountingAccountNumber: catalogCategory.accountingAccountNumber,
      usage: catalogCategory.usage,
      inactiv: catalogCategory.inactiv,
    });
  }

  protected createFromForm(): ICatalogCategoryFa {
    return {
      ...new CatalogCategoryFa(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      name: this.editForm.get(['name'])!.value,
      accountingAccountNumber: this.editForm.get(['accountingAccountNumber'])!.value,
      usage: this.editForm.get(['usage'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
    };
  }
}
