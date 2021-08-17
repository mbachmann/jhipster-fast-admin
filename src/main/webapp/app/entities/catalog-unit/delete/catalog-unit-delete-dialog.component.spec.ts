jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CatalogUnitService } from '../service/catalog-unit.service';

import { CatalogUnitDeleteDialogComponent } from './catalog-unit-delete-dialog.component';

describe('Component Tests', () => {
  describe('CatalogUnit Management Delete Component', () => {
    let comp: CatalogUnitDeleteDialogComponent;
    let fixture: ComponentFixture<CatalogUnitDeleteDialogComponent>;
    let service: CatalogUnitService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogUnitDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CatalogUnitDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogUnitDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CatalogUnitService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
