import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReportingActivity, ReportingActivity } from '../reporting-activity.model';
import { ReportingActivityService } from '../service/reporting-activity.service';
import { ICatalogService } from 'app/entities/catalog-service/catalog-service.model';
import { CatalogServiceService } from 'app/entities/catalog-service/service/catalog-service.service';

@Component({
  selector: 'fa-reporting-activity-update',
  templateUrl: './reporting-activity-update.component.html',
})
export class ReportingActivityUpdateComponent implements OnInit {
  isSaving = false;

  catalogServicesSharedCollection: ICatalogService[] = [];

  editForm = this.fb.group({
    id: [],
    remoteId: [null, []],
    name: [],
    useServicePrice: [],
    inactiv: [],
    catalogService: [],
  });

  constructor(
    protected reportingActivityService: ReportingActivityService,
    protected catalogServiceService: CatalogServiceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reportingActivity }) => {
      this.updateForm(reportingActivity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reportingActivity = this.createFromForm();
    if (reportingActivity.id !== undefined) {
      this.subscribeToSaveResponse(this.reportingActivityService.update(reportingActivity));
    } else {
      this.subscribeToSaveResponse(this.reportingActivityService.create(reportingActivity));
    }
  }

  trackCatalogServiceById(index: number, item: ICatalogService): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReportingActivity>>): void {
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

  protected updateForm(reportingActivity: IReportingActivity): void {
    this.editForm.patchValue({
      id: reportingActivity.id,
      remoteId: reportingActivity.remoteId,
      name: reportingActivity.name,
      useServicePrice: reportingActivity.useServicePrice,
      inactiv: reportingActivity.inactiv,
      catalogService: reportingActivity.catalogService,
    });

    this.catalogServicesSharedCollection = this.catalogServiceService.addCatalogServiceToCollectionIfMissing(
      this.catalogServicesSharedCollection,
      reportingActivity.catalogService
    );
  }

  protected loadRelationshipsOptions(): void {
    this.catalogServiceService
      .query()
      .pipe(map((res: HttpResponse<ICatalogService[]>) => res.body ?? []))
      .pipe(
        map((catalogServices: ICatalogService[]) =>
          this.catalogServiceService.addCatalogServiceToCollectionIfMissing(catalogServices, this.editForm.get('catalogService')!.value)
        )
      )
      .subscribe((catalogServices: ICatalogService[]) => (this.catalogServicesSharedCollection = catalogServices));
  }

  protected createFromForm(): IReportingActivity {
    return {
      ...new ReportingActivity(),
      id: this.editForm.get(['id'])!.value,
      remoteId: this.editForm.get(['remoteId'])!.value,
      name: this.editForm.get(['name'])!.value,
      useServicePrice: this.editForm.get(['useServicePrice'])!.value,
      inactiv: this.editForm.get(['inactiv'])!.value,
      catalogService: this.editForm.get(['catalogService'])!.value,
    };
  }
}
