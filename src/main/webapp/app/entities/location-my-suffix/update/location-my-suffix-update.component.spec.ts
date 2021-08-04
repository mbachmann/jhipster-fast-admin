jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { LocationMySuffixService } from '../service/location-my-suffix.service';
import { ILocationMySuffix, LocationMySuffix } from '../location-my-suffix.model';
import { IDepartmentMySuffix } from 'app/entities/department-my-suffix/department-my-suffix.model';
import { DepartmentMySuffixService } from 'app/entities/department-my-suffix/service/department-my-suffix.service';

import { LocationMySuffixUpdateComponent } from './location-my-suffix-update.component';

describe('Component Tests', () => {
  describe('LocationMySuffix Management Update Component', () => {
    let comp: LocationMySuffixUpdateComponent;
    let fixture: ComponentFixture<LocationMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let locationService: LocationMySuffixService;
    let departmentService: DepartmentMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LocationMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(LocationMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LocationMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      locationService = TestBed.inject(LocationMySuffixService);
      departmentService = TestBed.inject(DepartmentMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DepartmentMySuffix query and add missing value', () => {
        const location: ILocationMySuffix = { id: 456 };
        const department: IDepartmentMySuffix = { id: 52239 };
        location.department = department;

        const departmentCollection: IDepartmentMySuffix[] = [{ id: 46307 }];
        jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
        const additionalDepartmentMySuffixes = [department];
        const expectedCollection: IDepartmentMySuffix[] = [...additionalDepartmentMySuffixes, ...departmentCollection];
        jest.spyOn(departmentService, 'addDepartmentMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ location });
        comp.ngOnInit();

        expect(departmentService.query).toHaveBeenCalled();
        expect(departmentService.addDepartmentMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          departmentCollection,
          ...additionalDepartmentMySuffixes
        );
        expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const location: ILocationMySuffix = { id: 456 };
        const department: IDepartmentMySuffix = { id: 4472 };
        location.department = department;

        activatedRoute.data = of({ location });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(location));
        expect(comp.departmentsSharedCollection).toContain(department);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LocationMySuffix>>();
        const location = { id: 123 };
        jest.spyOn(locationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ location });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: location }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(locationService.update).toHaveBeenCalledWith(location);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LocationMySuffix>>();
        const location = new LocationMySuffix();
        jest.spyOn(locationService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ location });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: location }));
        saveSubject.complete();

        // THEN
        expect(locationService.create).toHaveBeenCalledWith(location);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<LocationMySuffix>>();
        const location = { id: 123 };
        jest.spyOn(locationService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ location });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(locationService.update).toHaveBeenCalledWith(location);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDepartmentMySuffixById', () => {
        it('Should return tracked DepartmentMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDepartmentMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
