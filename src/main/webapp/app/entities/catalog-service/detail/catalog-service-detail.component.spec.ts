import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogServiceDetailComponent } from './catalog-service-detail.component';

describe('Component Tests', () => {
  describe('CatalogService Management Detail Component', () => {
    let comp: CatalogServiceDetailComponent;
    let fixture: ComponentFixture<CatalogServiceDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogServiceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogService: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogServiceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogServiceDetailComponent);
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
