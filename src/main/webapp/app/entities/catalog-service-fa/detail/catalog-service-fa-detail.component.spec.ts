import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogServiceFaDetailComponent } from './catalog-service-fa-detail.component';

describe('Component Tests', () => {
  describe('CatalogServiceFa Management Detail Component', () => {
    let comp: CatalogServiceFaDetailComponent;
    let fixture: ComponentFixture<CatalogServiceFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogServiceFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogService: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogServiceFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogServiceFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load catalogService on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.catalogService).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
