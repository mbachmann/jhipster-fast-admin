jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CatalogServiceFaService } from '../service/catalog-service-fa.service';

import { CatalogServiceFaDeleteDialogComponent } from './catalog-service-fa-delete-dialog.component';

describe('Component Tests', () => {
  describe('CatalogServiceFa Management Delete Component', () => {
    let comp: CatalogServiceFaDeleteDialogComponent;
    let fixture: ComponentFixture<CatalogServiceFaDeleteDialogComponent>;
    let service: CatalogServiceFaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogServiceFaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CatalogServiceFaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogServiceFaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CatalogServiceFaService);
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
