import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LayoutFaService } from '../service/layout-fa.service';

import { LayoutFaComponent } from './layout-fa.component';

describe('Component Tests', () => {
  describe('LayoutFa Management Component', () => {
    let comp: LayoutFaComponent;
    let fixture: ComponentFixture<LayoutFaComponent>;
    let service: LayoutFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [LayoutFaComponent],
      })
        .overrideTemplate(LayoutFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LayoutFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(LayoutFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.layouts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
